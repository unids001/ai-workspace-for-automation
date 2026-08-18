# Pruebas `GET /users/{id}`

## Checklist de Desarrollo

- [ ] Revisar contrato y endpoint: `GET /api/v1/users/{id}`
- [ ] Crear feature en `runner/src/test/resources/features/api/user/`
- [ ] Escribir escenarios: happy path (200) + negativo (404, etc)
- [ ] Crear/usar Task Screenplay: `GetUser.using(userId)`
- [ ] Crear/usar DTO: `GetUserResponse`
- [ ] Crear/usar mappings WireMock: `get_user_response.json`
- [ ] Crear/usar `__files`: `sample_user_response.json`
- [ ] Añadir step definitions reutilizables
- [ ] Validar: status, id, username, email, role, active, locale
- [ ] Validar errores controlados en casos negativos
- [ ] Sin waits artificiales, sin acoplamiento, sin datos hardcodeados

## Criterios de Salida

✓ Escenario feliz: GET exitoso con 200  
✓ Escenario negativo: error controlado (404, etc)  
✓ Validaciones explícitas de estado y campos críticos  
✓ Escenarios independientes y legibles  
✓ Alineado con dominio de usuarios
