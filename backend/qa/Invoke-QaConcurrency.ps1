#requires -Version 7.0
[CmdletBinding()]
param([Parameter(Mandatory)][string]$ConfigPath)
$ErrorActionPreference = 'Stop'
$config = Get-Content -LiteralPath $ConfigPath -Raw | ConvertFrom-Json
foreach ($key in @('url','inventoryUrl','body','beforeQuantity','expectedAfterQuantity','mode')) {
    if ($null -eq $config.$key) { throw "Falta $key en el archivo de configuración." }
}
if ($config.mode -notin @('LastUnit','Idempotent')) { throw 'mode debe ser LastUnit o Idempotent.' }
$allowedHost = 'api-dev.juanodsk.dev'
foreach ($address in @($config.url, $config.inventoryUrl)) {
    $uri = [uri]$address
    if ($uri.Scheme -ne 'https' -or $uri.Host -ne $allowedHost -or $uri.UserInfo -or $uri.Query) {
        throw 'Solo se permiten URLs HTTPS de api-dev sin credenciales ni query.'
    }
}
if ($config.url -notmatch '/api/v1/businesses/\d+/(sales|repair-orders/\d+/parts|stock-movements)$') {
    throw 'URL POST no admitida para el escenario de concurrencia.'
}
if ($config.inventoryUrl -notmatch '/api/v1/businesses/\d+/inventory-items/\d+$') {
    throw 'inventoryUrl debe ser GET del artículo específico.'
}
$config.url -match '/businesses/(\d+)' | Out-Null
$postBusiness = $Matches[1]
$config.inventoryUrl -match '/businesses/(\d+)' | Out-Null
if ($postBusiness -ne $Matches[1]) { throw 'POST e inventario deben pertenecer al mismo negocio QA.' }
$secureToken = Read-Host 'Access token del actor QA (no se guarda)' -AsSecureString
$token = [System.Net.NetworkCredential]::new('', $secureToken).Password
$headers = @{ Authorization = "Bearer $token"; 'Accept-Language' = 'es' }
$before = Invoke-RestMethod -Uri $config.inventoryUrl -Headers $headers -Method Get -TimeoutSec 30
if ($before.data.quantity -ne $config.beforeQuantity) { throw 'Cantidad inicial inesperada. No se enviaron POST.' }
$body = $config.body | ConvertTo-Json -Depth 20 -Compress
$sameKey = [guid]::NewGuid().ToString()
$requests = 1..2 | ForEach-Object {
    [pscustomobject]@{ Url=$config.url; Body=$body; Key= $(if ($config.mode -eq 'Idempotent') { $sameKey } else { [guid]::NewGuid().ToString() }) }
}
$results = @($requests | ForEach-Object -Parallel {
    $taskToken = $using:token
    $request = $_
    $watch = [System.Diagnostics.Stopwatch]::StartNew()
    try {
        $reply = Invoke-WebRequest -Uri $request.Url -Method Post -ContentType 'application/json' -Body $request.Body -Headers @{Authorization="Bearer $taskToken"; 'Idempotency-Key'=$request.Key} -SkipHttpErrorCheck -TimeoutSec 30 -MaximumRedirection 0
        $json = $reply.Content | ConvertFrom-Json
        [pscustomobject]@{status=[int]$reply.StatusCode; id=$json.data.id; elapsedMs=$watch.ElapsedMilliseconds; error=$null}
    } catch {
        # Do not export raw exceptions: they may contain request or transport details.
        [pscustomobject]@{status=0; id=$null; elapsedMs=$watch.ElapsedMilliseconds; error=$_.Exception.GetType().Name}
    } finally { $taskToken=$null }
} -ThrottleLimit 2)
$after = Invoke-RestMethod -Uri $config.inventoryUrl -Headers $headers -Method Get -TimeoutSec 30
$token = $null
$headers.Clear()
$actual = @($results.status | Sort-Object)
$expected = if ($config.mode -eq 'LastUnit') { @(201,409) } else { @(201,201) }
$pass = (($actual -join ',') -eq ($expected -join ',')) -and ($after.data.quantity -eq $config.expectedAfterQuantity)
if ($config.mode -eq 'Idempotent') { $pass = $pass -and ($null -ne $results[0].id) -and ($results[0].id -eq $results[1].id) }
[pscustomobject]@{mode=$config.mode; passed=$pass; beforeQuantity=$before.data.quantity; afterQuantity=$after.data.quantity; requests=$results; manualCheck='Confirmar un solo movimiento y recurso efectivo mediante GET; sin esta revisión no marcar PASS final.'} | ConvertTo-Json -Depth 8
if (-not $pass) { exit 1 }
