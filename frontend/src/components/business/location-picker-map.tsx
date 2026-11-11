"use client";

import { Crosshair, LocateFixed, MapPin, Trash2 } from "lucide-react";
import { Map as MapLibreMap, Marker as MapLibreMarker, NavigationControl, type StyleSpecification } from "maplibre-gl";
import { useCallback, useEffect, useRef, useState } from "react";

const DEFAULT_CENTER = { lat: 4.5709, lng: -74.2973 };
const LOCATION_TIMEOUT_MS = 15_000;
const MAP_RENDER_TIMEOUT_MS = 30_000;
const DEFAULT_MAP_STYLE = "osm-bright";

function coordinate(value: string) {
  if (!value.trim()) return null;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
}

function geolocationMessage(error: GeolocationPositionError) {
  if (error.code === error.PERMISSION_DENIED) return "No autorizaste el acceso a tu ubicación. Puedes marcarla directamente en el mapa.";
  if (error.code === error.POSITION_UNAVAILABLE) return "Tu dispositivo no pudo determinar la ubicación. Puedes marcarla directamente en el mapa.";
  if (error.code === error.TIMEOUT) return "La ubicación tardó demasiado en responder. Inténtalo nuevamente o marca el punto en el mapa.";
  return "No pudimos obtener tu ubicación. Puedes marcarla directamente en el mapa.";
}

function zoomForAccuracy(accuracy: number) {
  if (accuracy <= 50) return 18;
  if (accuracy <= 150) return 17;
  if (accuracy <= 500) return 16;
  if (accuracy <= 2_000) return 14;
  return 11;
}

function accuracyMessage(accuracy: number) {
  if (accuracy < 1_000) return `Precisión estimada del dispositivo: ±${Math.max(1, Math.round(accuracy))} m.`;
  return `El navegador solo pudo aproximar tu ubicación a ±${(accuracy / 1_000).toFixed(1)} km. Ajusta el pin antes de guardar.`;
}

function geoapifyStyle(apiKey: string): StyleSpecification {
  const configuredStyle = process.env.NEXT_PUBLIC_GEOAPIFY_MAP_STYLE?.trim() || DEFAULT_MAP_STYLE;
  const tileUrl = `https://maps.geoapify.com/v1/tile/${encodeURIComponent(configuredStyle)}/{z}/{x}/{y}.png?apiKey=${encodeURIComponent(apiKey)}`;

  return {
    version: 8,
    sources: {
      geoapify: {
        type: "raster",
        tiles: [tileUrl],
        tileSize: 256,
        maxzoom: 20,
        attribution: 'Powered by <a href="https://www.geoapify.com/">Geoapify</a> | <a href="https://openmaptiles.org/">© OpenMapTiles</a> <a href="https://www.openstreetmap.org/copyright">© OpenStreetMap</a> contributors',
      },
    },
    layers: [{ id: "geoapify-basemap", type: "raster", source: "geoapify" }],
  };
}

function createMarkerElement() {
  const markerElement = document.createElement("div");
  markerElement.className = "tm-location-marker";
  markerElement.setAttribute("aria-label", "Ubicación seleccionada para la sede");
  markerElement.innerHTML = '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M20 10c0 5-8 12-8 12S4 15 4 10a8 8 0 1 1 16 0Z"/><circle cx="12" cy="10" r="2.5"/></svg>';
  return markerElement;
}

export function LocationPickerMap({
  latitude,
  longitude,
  error,
  onChange,
  onClear,
}: {
  latitude: string;
  longitude: string;
  error?: string;
  onChange: (latitude: string, longitude: string) => void;
  onClear: () => void;
}) {
  const containerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<MapLibreMap | null>(null);
  const markerRef = useRef<MapLibreMarker | null>(null);
  const watchIdRef = useRef<number | null>(null);
  const watchTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const mapRenderTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const bestAccuracyRef = useRef(Number.POSITIVE_INFINITY);
  const onChangeRef = useRef(onChange);
  const [ready, setReady] = useState(false);
  const [mapBusy, setMapBusy] = useState(true);
  const [mapError, setMapError] = useState<string | null>(null);
  const [canRetryMap, setCanRetryMap] = useState(false);
  const [retryVersion, setRetryVersion] = useState(0);
  const [locating, setLocating] = useState(false);
  const [locationError, setLocationError] = useState<string | null>(null);
  const [locationAccuracy, setLocationAccuracy] = useState<number | null>(null);
  const selectedLatitude = coordinate(latitude);
  const selectedLongitude = coordinate(longitude);
  const hasSelection = selectedLatitude !== null && selectedLongitude !== null;

  useEffect(() => {
    onChangeRef.current = onChange;
  }, [onChange]);

  const stopLocationWatch = useCallback(() => {
    if (watchIdRef.current !== null) navigator.geolocation?.clearWatch(watchIdRef.current);
    if (watchTimeoutRef.current !== null) clearTimeout(watchTimeoutRef.current);
    watchIdRef.current = null;
    watchTimeoutRef.current = null;
    setLocating(false);
  }, []);

  const placeMarker = useCallback((lat: number, lng: number, centerMap = false, zoom = 17) => {
    const map = mapRef.current;
    if (!map) return;

    if (!markerRef.current) {
      const marker = new MapLibreMarker({
        element: createMarkerElement(),
        draggable: true,
        anchor: "bottom",
      })
        .setLngLat([lng, lat])
        .addTo(map);
      marker.on("dragend", () => {
        const position = marker.getLngLat();
        setLocationAccuracy(null);
        onChangeRef.current(position.lat.toFixed(7), position.lng.toFixed(7));
      });
      markerRef.current = marker;
    } else {
      markerRef.current.setLngLat([lng, lat]);
    }

    if (centerMap) {
      map.easeTo({
        center: [lng, lat],
        zoom,
        duration: window.matchMedia("(prefers-reduced-motion: reduce)").matches ? 0 : 550,
      });
    }
  }, []);

  useEffect(() => {
    const container = containerRef.current;
    if (!container || mapRef.current) return;
    let active = true;
    let mapLoaded = false;
    const apiKey = process.env.NEXT_PUBLIC_GEOAPIFY_API_KEY?.trim();

    if (!apiKey) {
      setMapBusy(false);
      setMapError("Geoapify no está configurado. Agrega NEXT_PUBLIC_GEOAPIFY_API_KEY y reinicia el frontend.");
      setCanRetryMap(false);
      return;
    }

    try {
      const initialLat = coordinate(latitude);
      const initialLng = coordinate(longitude);
      const initialCenter: [number, number] = initialLat !== null && initialLng !== null
        ? [initialLng, initialLat]
        : [DEFAULT_CENTER.lng, DEFAULT_CENTER.lat];

      const map = new MapLibreMap({
        container,
        style: geoapifyStyle(apiKey),
        center: initialCenter,
        zoom: initialLat !== null ? 16 : 5,
        attributionControl: { compact: true },
        cooperativeGestures: true,
        maxZoom: 20,
      });
      mapRef.current = map;
      map.addControl(new NavigationControl({ showCompass: false }), "bottom-right");

      map.on("click", (event) => {
        const { lat, lng } = event.lngLat;
        setLocationAccuracy(null);
        placeMarker(lat, lng);
        onChangeRef.current(lat.toFixed(7), lng.toFixed(7));
      });

      map.on("error", (event) => {
        if (!active || mapLoaded) return;
        const message = event.error?.message?.toLowerCase() || "";
        if (!message.includes("401") && !message.includes("403") && !message.includes("api key") && !message.includes("unauthorized")) return;
        if (mapRenderTimeoutRef.current !== null) clearTimeout(mapRenderTimeoutRef.current);
        setMapBusy(false);
        setMapError("Geoapify rechazó la credencial. Verifica la API key y los dominios autorizados.");
        setCanRetryMap(true);
      });

      map.once("load", () => {
        if (!active) return;
        mapLoaded = true;
        if (mapRenderTimeoutRef.current !== null) clearTimeout(mapRenderTimeoutRef.current);
        mapRenderTimeoutRef.current = null;
        if (initialLat !== null && initialLng !== null) placeMarker(initialLat, initialLng);
        setMapBusy(false);
        setMapError(null);
        setCanRetryMap(false);
        setReady(true);
      });

      mapRenderTimeoutRef.current = setTimeout(() => {
        if (!active || mapLoaded) return;
        setMapBusy(false);
        setMapError("El mapa no pudo iniciar. Revisa tu conexión e inténtalo nuevamente.");
        setCanRetryMap(true);
      }, MAP_RENDER_TIMEOUT_MS);
    } catch {
      setMapBusy(false);
      setMapError("No pudimos iniciar el mapa de Geoapify. Verifica la configuración e inténtalo nuevamente.");
      setCanRetryMap(true);
    }

    return () => {
      active = false;
      stopLocationWatch();
      if (mapRenderTimeoutRef.current !== null) clearTimeout(mapRenderTimeoutRef.current);
      mapRenderTimeoutRef.current = null;
      markerRef.current?.remove();
      markerRef.current = null;
      mapRef.current?.remove();
      mapRef.current = null;
    };
    // La posición inicial solo se consume al crear el mapa; los cambios posteriores se sincronizan abajo.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [placeMarker, retryVersion, stopLocationWatch]);

  useEffect(() => {
    if (!ready) return;
    if (selectedLatitude === null || selectedLongitude === null) {
      markerRef.current?.remove();
      markerRef.current = null;
      return;
    }
    placeMarker(selectedLatitude, selectedLongitude);
  }, [placeMarker, ready, selectedLatitude, selectedLongitude]);

  function markCenter() {
    const center = mapRef.current?.getCenter();
    if (!center) return;
    setLocationError(null);
    setLocationAccuracy(null);
    placeMarker(center.lat, center.lng);
    onChange(center.lat.toFixed(7), center.lng.toFixed(7));
  }

  function useCurrentLocation() {
    setLocationError(null);
    setLocationAccuracy(null);
    if (!navigator.geolocation) {
      setLocationError("Este navegador no permite obtener tu ubicación. Puedes marcarla directamente en el mapa.");
      return;
    }

    stopLocationWatch();
    bestAccuracyRef.current = Number.POSITIVE_INFINITY;
    setLocating(true);
    watchIdRef.current = navigator.geolocation.watchPosition(
      (position) => {
        const { accuracy, latitude: lat, longitude: lng } = position.coords;
        if (accuracy >= bestAccuracyRef.current) return;
        bestAccuracyRef.current = accuracy;
        setLocationAccuracy(accuracy);
        placeMarker(lat, lng, true, zoomForAccuracy(accuracy));
        onChangeRef.current(lat.toFixed(7), lng.toFixed(7));
        if (accuracy <= 50) stopLocationWatch();
      },
      (requestError) => {
        setLocationError(geolocationMessage(requestError));
        stopLocationWatch();
      },
      { enableHighAccuracy: true, timeout: LOCATION_TIMEOUT_MS, maximumAge: 0 },
    );
    watchTimeoutRef.current = setTimeout(stopLocationWatch, LOCATION_TIMEOUT_MS);
  }

  function clearSelection() {
    stopLocationWatch();
    markerRef.current?.remove();
    markerRef.current = null;
    setLocationError(null);
    setLocationAccuracy(null);
    onClear();
  }

  function retryMap() {
    setReady(false);
    setMapBusy(true);
    setMapError(null);
    setRetryVersion((version) => version + 1);
  }

  return (
    <div className="tm-location-picker">
      <header>
        <div>
          <strong>Marca la entrada de la sede</strong>
          <p>Haz clic en el mapa o arrastra el pin hasta el punto exacto.</p>
        </div>
        <div>
          <button className="tm-button tm-button--secondary" type="button" onClick={useCurrentLocation} disabled={!ready || mapBusy || locating}>
            <LocateFixed aria-hidden="true" /> {locating ? "Mejorando precisión…" : "Usar mi ubicación"}
          </button>
          <button className="tm-button tm-button--secondary" type="button" onClick={markCenter} disabled={!ready || mapBusy}>
            <Crosshair aria-hidden="true" /> Marcar centro
          </button>
        </div>
      </header>
      <div className="tm-location-picker__canvas">
        <div
          ref={containerRef}
          className="tm-location-map"
          data-ready={ready ? "true" : "false"}
          aria-label="Mapa de Geoapify para seleccionar la ubicación exacta de la sede"
          aria-describedby="tm-location-map-help"
        />
        {mapError ? (
          <div className="tm-location-picker__loading tm-location-picker__loading--error" role="alert">
            <p>{mapError}</p>
            {canRetryMap ? <button className="tm-button tm-button--secondary" type="button" onClick={retryMap}>Reintentar mapa</button> : null}
          </div>
        ) : mapBusy || !ready ? (
          <div className="tm-location-picker__loading" aria-live="polite">Cargando mapa…</div>
        ) : null}
      </div>
      <footer id="tm-location-map-help">
        <MapPin aria-hidden="true" />
        <p aria-live="polite">
          <strong>{hasSelection ? "Ubicación seleccionada" : "Ubicación exacta pendiente"}</strong>
          <span>{hasSelection ? `${selectedLatitude.toFixed(7)}, ${selectedLongitude.toFixed(7)}` : "Mueve el mapa y usa “Marcar centro” si navegas con teclado."}</span>
        </p>
        {hasSelection ? <button type="button" onClick={clearSelection}><Trash2 aria-hidden="true" /> Quitar ubicación</button> : null}
      </footer>
      {locationAccuracy !== null ? (
        <p className={`tm-location-picker__accuracy${locationAccuracy > 1_000 ? " tm-location-picker__accuracy--warning" : ""}`} aria-live="polite">
          <LocateFixed aria-hidden="true" /> {accuracyMessage(locationAccuracy)}
        </p>
      ) : null}
      {locationError ? <p className="tm-location-picker__error" role="alert">{locationError}</p> : null}
      {error ? <p className="tm-location-picker__error" role="alert">{error}</p> : null}
    </div>
  );
}
