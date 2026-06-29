# Codeshare Management Frontend

Angular standalone application for the Codeshare Management administration UI.

## App Structure

```text
src/app/
  core/       App-wide API, security, guards, interceptors, and singleton services
  features/   Domain pages, routes, feature services, and feature models
  layout/     Shell, navigation, menu, and layout services
  shared/     Reusable components, directives, models, types, and PrimeNG imports
```

## Feature Structure

Keep each feature self-contained:

```text
feature-name/
  pages/
    entity-list/
    entity-form/
  services/
  models/
  feature.routes.ts
```

Access Management currently follows this pattern under `src/app/features/access-management/iam`.

## Shared Code Rules

- Put reusable contracts in `src/app/shared/models`.
- Put reusable UI primitives in `src/app/shared/components`.
- Put permission-only DOM behavior in `src/app/shared/directives`.
- Keep API calls inside feature services unless the service is truly app-wide.

## Common Base Classes

- `BaseListComponent<T>` centralizes list loading, refresh, loading state, and read permission checks.
- `BaseCrudForm<T>` centralizes create/edit form initialization, submit, cancel, and loading state.

Feature pages should keep only feature-specific actions, mapping, and table/form state.

## Commands

```bash
npm start
npm run build
npm test
```

The development server runs at `http://localhost:4200/` by default.
