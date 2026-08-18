# Purpose
Generate Java POJO classes from JSON payloads for API test automation.

## Required input
Provide:
1. **JSON sample**
2. **Model purpose**
   - `modelType`: `request` or `response`
   - `rootClassName`: e.g. `CreateUserRequest`, `CreateUserResponse`
   - `featureName`: e.g. `users/create`
3. **Target package/path**
   - Use the project's real package structure for models.
4. **Optional mapping hints**
   - required/nullable fields
   - JSON names different from Java names
   - preferred types (`BigDecimal`, `Integer`, `LocalDateTime`, etc.)

## Output rules
- Generate complete Java files (package, imports, class, fields).
- Keep classes as pure models (no business logic).
- Always include:
  - `@Data`
  - `@JsonIgnoreProperties(ignoreUnknown = true)`
- Add `@Builder` by default for **request** models.
- For **response** models, add `@Builder` only if requested.
- Use `@JsonProperty` when JSON and Java names differ.
- Map arrays as `List<T>`.
- For nested objects, create additional POJO classes when needed.

## Naming rules
- Class names: PascalCase
- Field names: camelCase
- Use explicit suffixes: `Request`, `Response`, `Data`, `Item`

## Expected response format
1. **Generated files** (one bullet per file with full path)
2. **Java code blocks** (one block per file)
3. **Short mapping notes** (`@JsonProperty` usage and custom type choices)

## Quality checklist
- [ ] Package path matches the real project structure
- [ ] Class names match the model purpose
- [ ] `@Data` is present
- [ ] `@JsonIgnoreProperties(ignoreUnknown = true)` is present
- [ ] `@Builder` follows model type rule
- [ ] Arrays use `List<T>`
- [ ] Mismatched JSON names use `@JsonProperty`
- [ ] No service logic

## Example A (Generic request)
**Input:**
```text
modelType: request
rootClassName: CreateUserRequest
featureName: users/create

JSON:
{
  "username": "jdoe",
  "email": "jdoe@example.com",
  "active": true
}
```

**Output:**
```java
package <base-package>.model.users.create.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {
    private String username;
    private String email;
    private Boolean active;
}
```

## Example B (Generic response with nested object)
**Input:**
```text
modelType: response
rootClassName: LoginResponse
featureName: auth/login

JSON:
{
  "data": {
    "token": "abc123",
    "expiresIn": 3600
  }
}
```

**Output file 1:**
```java
package <base-package>.model.auth.login.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private LoginData data;
}
```

**Output file 2:**
```java
package <base-package>.model.auth.login.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData {
    private String token;
    private Integer expiresIn;
}
```
