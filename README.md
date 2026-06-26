# codeshare-management

Codeshare Management is split into two main applications:

- `front-end`: Angular standalone application for administration screens.
- `back-end`: Spring Boot multi-module backend for gateway, identity, master data, and schedule services.

## Repository Map

```text
codeshare-management/
  front-end/    Angular UI
  back-end/     Spring Boot backend modules
  documents/    Product and design notes
```

## Development Flow

Start with the frontend when working on pages, routing, forms, tables, or UI permissions. Start with the backend when changing API contracts, database entities, service rules, or authentication behavior.

Use feature folders for domain-specific code and shared folders only for code that is reused by multiple features. Avoid placing feature logic in shared utilities just because it is convenient; shared code should be stable and broadly useful.

## Naming Conventions

- Page components end with `.page.ts`, `.page.html`, and `.page.scss`.
- Reusable visual components end with `.component.ts`.
- API wrappers end with `.service.ts`.
- Data contracts end with `.model.ts`.
- Backend controllers, services, repositories, and entities stay in their matching package layers.

## Verification

Frontend:

```bash
cd front-end
npm run build
```

Backend:

```bash
cd back-end
./mvnw test
```
