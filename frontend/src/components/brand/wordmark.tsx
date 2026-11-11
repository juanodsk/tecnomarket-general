import Link from "next/link";

export function Wordmark({ href = "/" }: { href?: string }) {
  return (
    <Link className="tm-wordmark" href={href} aria-label="TecMarket, ir al inicio">
      <span className="tm-wordmark__mark" aria-hidden="true">
        T
      </span>
      <span>TecMarket</span>
    </Link>
  );
}
