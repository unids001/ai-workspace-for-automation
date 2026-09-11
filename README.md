# ai-workspace-for-automation

API automation workspace built with **Serenity BDD + Cucumber + Screenplay**. It exists to validate service behavior through repeatable mock-driven scenarios, with shared test data, reusable Tasks, and a runner that boots everything locally.

**Built for AI agents**: This project is designed as a Copilot-native workspace with an embedded AI ecosystem that enables agents (and specialized subagents) to understand, navigate, and extend the codebase autonomously through explicit context files, clear code paths, and domain-specific role definitions.

The core idea is simple: define the business flow in Gherkin, resolve controlled test users, send the request through a Screenplay Task, and validate the response from WireMock—all in a structure that AI agents can reason about and modify safely.

## Table of Contents

1. [Project Overview](#project-overview)
2. [AI Ecosystem & Copilot Workspace](#ai-ecosystem--copilot-workspace)
3. [Getting Started](#getting-started)
4. [Usage Guide](#usage-guide)
5. [Project Structure & Architecture](#project-structure--architecture)
6. [Support & Contribution](#support--contribution)

## Project Overview

**ai-workspace-for-automation** is a Maven multi-module workspace for API test automation, built to validate service behavior through repeatable mock-driven scenarios.

It contains:

- `api/`: reusable Screenplay Tasks, request/response models, and route constants
- `runner/`: Cucumber features, step definitions, hooks, WireMock stubs, and test data
- The workspace is open to future modules, including mobile testing, as the automation scope grows.

**Why it exists**:

- to keep API tests deterministic
- to avoid coupling scenarios to live dependencies
- to centralize test users, request contracts, and endpoint behavior
- to make the test suite readable for new contributors
- **to showcase how AI agents (Copilot, Claude, and others) can interact with and extend a test automation codebase** through an embedded AI ecosystem with context files, specialized subagents, and explicit guardrails

## AI Ecosystem & Copilot Workspace

This project is designed as a **Copilot-native workspace** that showcases how AI agents interact with test automation infrastructure. The architecture enables agents to understand, modify, and extend the codebase without manual context switching.

### The Big Picture: High-Level Pipeline

```
User Request
    ↓
Copilot CLI / Main Agent
    ↓
Code exploration (grep, glob, view)
    ↓
Context loading (global-context.md, domain specs)
    ↓
Scope detection (task service? user service? mobile?)
    ↓
Subagent spawning (if specialized work detected)
    ├─→ task-api-specialist (task service API tests)
    ├─→ user-api-specialist (user service API tests)
    └─→ future: mobile-specialist, performance-specialist
    ↓
Plan generation (understanding scope & constraints)
    ↓
Implementation (create/edit files, run tests)
    ↓
Verification (bash commands, build, validate)
    ↓
Documentation (update README, maintain clarity)
```

### Component Breakdown

The workspace is divided into **agent-friendly layers**:

1. **Static Context Layer**
   - `./ai/global-context.md` — master rules and conventions (loaded by ALL agents)
   - `./.github/instructions/` — domain-specific guidance (e.g., `task-api-specialist.md`)
   - Enables agents to load project culture before acting
   - Each subagent inherits global rules + its own specialized constraints

2. **Code Layer**
   - `api/` — reusable contracts and tasks (DRY principle)
   - `runner/` — scenario execution, test data, configuration
   - Organized by functional domain, not technical type
   - Minimal coupling for easy agent navigation
   - Subagents focus on specific domains (e.g., task-api-specialist owns `runner/**/task/**` paths)

3. **Execution Layer**
   - `runner/src/test/resources/features/` — Gherkin scenarios (human-readable intent)
   - `runner/src/test/java/com/testing/training/stepdefinitions/` — Bridge to code logic
   - `api/src/main/java/com/testing/training/tasks/` — Reusable Screenplay tasks
   - Clear separation of concerns reduces agent confusion
   - Subagents have explicit boundaries (e.g., task-api-specialist modifies task tests only)

4. **Data & Mock Layer**
   - `runner/src/test/resources/data/usertestdata.json` — Centralized test users
   - `runner/src/test/resources/wiremock/` — Deterministic service stubs
   - No external dependencies, fully agent-controlled

### Step-by-Step Data Cycle

**When an agent (or subagent) processes a test flow:**

1. **Discovery Phase**
   - Agent reads the feature file (e.g., `wiremock_login.feature`)
   - Identifies steps, tags, and domain scope (e.g., `@successfulLogin` = user service)
   - Traces to step definition class
   - **Subagent detection**: If task-related, main agent spawns task-api-specialist

2. **Context Resolution Phase**
   - Agent loads global-context.md + its domain-specific .md file (e.g., task-api-specialist.md)
   - Understands role, scope boundaries, and non-negotiable constraints
   - Reads `LocalTestData` to understand user resolution
   - Reads `serenity.conf` to determine active environment
   - Resolves test data from JSON structure

3. **Task Analysis Phase**
   - Agent inspects the `Login` Task to understand request shape
   - Reviews `LoginRequest` model for contract
   - Maps endpoint via `RestServiceConfig`
   - **Subagent scope check**: Verifies work is within domain (e.g., task-api-specialist only touches task APIs)

4. **Stub Validation Phase**
   - Agent checks WireMock mapping under `wiremock/mappings/`
   - Verifies fixture response under `wiremock/__files/`
   - Confirms request/response alignment

5. **Assertion Review Phase**
   - Agent examines step definitions for validation logic
   - Ensures assertions cover business intent and negative cases
   - Identifies gaps for enhancement
   - **Subagent guardrails**: Keeps assertions stable and repeatable (no hardcoded volatile data)

6. **Execution & Feedback Phase**
   - Agent runs `mvn verify` to exercise the flow
   - Observes console output and test results
   - Updates code, docs, or tests based on findings
   - Reports results back to main agent (if subagent)

### Why This Design Matters for AI

- **Traceable paths**: Each layer has clear entry and exit points for agent navigation
- **Minimal magic**: No implicit behavior; everything is explicit and discoverable
- **Self-documenting**: Gherkin, task names, and model fields act as inline documentation
- **Deterministic**: No flaky timing or external dependencies cloud the analysis
- **Modular growth**: New features (mobile, performance, security) can be added as new modules without refactoring core
- **Subagent scalability**: Specialized agents (task-api-specialist, user-api-specialist, mobile-specialist) have clear role definitions, scope boundaries, and non-negotiable constraints loaded from their own .md files
- **Parallel specialization**: Each subagent inherits global rules but operates independently, allowing safe concurrent work on different domains

### Extending the Workspace

This workspace is **open to other AI frameworks** beyond Copilot and supports **domain-specific subagents**. The structure is adaptable to:

- Claude projects (via the same context files)
- ChatGPT plugins or custom agents
- GitHub Actions automation
- GitLab CI/CD pipelines
- **New subagents**: Add a .md file under `./ai/agents/` with role, scope, source code paths, and constraints

**Example subagent structure:**
```
./ai/agents/
  ├── task-pod/
  │   └── task-api-specialist.md       ← Tests for task service
  ├── user-pod/
  │   └── user-api-specialist.md       ← Tests for user service
  └── mobile-pod/
      └── mobile-specialist.md         ← Mobile UI tests (future)
```

The key principle: **AI reads code the same way humans do—through clear paths, explicit constraints, and minimal surprises.**

## Getting Started

### Requirements

- Java 17
- Maven 3.8+

### Run in one command

From the project root:

```bash
mvn -q -pl runner -am verify
```

This starts the suite through the `runner` module, boots the WireMock server via hooks, executes the Cucumber scenarios, and generates Serenity output.

### Run only the login flow

```bash
mvn -q -pl runner -am verify -Dcucumber.filter.tags="@successfulLogin"
```

## Usage Guide

### What happens in the successful login route

The `@successfulLogin` scenario follows this path:

1. `wiremock_login.feature` calls `When I login with username "<alias>"`.
2. `MockUserServerStepDefinitions` resolves the alias with `LocalTestData`.
3. `LocalTestData` reads `runner/src/test/resources/data/usertestdata.json`.
4. The current environment is read from Serenity config (`api-qa -> qa` by default).
5. The step builds a `LoginRequest` with the selected username and password.
6. The `Login` Task sends a `POST` to `/api/v1/auth/login`.
7. WireMock returns a templated response with the same username and a `Bearer` token type.
8. The step definitions assert status, username, and token type.

### Test data

Test users are stored in `runner/src/test/resources/data/usertestdata.json`.

Available aliases:

- Tyrion
- Jon Snow
- Daenerys

Each alias has environment-specific credentials for `dev` and `qa`.

### Mock server

WireMock is started and stopped automatically by `Hooks`.

- `@BeforeAll`: starts the mock server
- `@Before`: binds the actor to the mock server URL
- `@AfterAll`: stops the mock server and clears the system property

## Project Structure & Architecture

### High-level architecture

- **Gherkin** describes the business flow.
- **Step definitions** translate the scenario into Screenplay actions.
- **Tasks** encapsulate HTTP calls and keep request logic reusable.
- **Models** define request and response contracts.
- **WireMock** provides deterministic service behavior.

### Main paths

| Area | Main path |
| --- | --- |
| Features | `runner/src/test/resources/features/` |
| Step definitions | `runner/src/test/java/com/testing/training/stepdefinitions/` |
| Hooks | `runner/src/test/java/com/testing/training/stepdefinitions/commons/` |
| Test data | `runner/src/test/resources/data/` |
| Data loader | `runner/src/test/java/com/testing/training/testdata/manager/` |
| Runner | `runner/src/test/java/com/testing/training/runner/` |
| API tasks | `api/src/main/java/com/testing/training/tasks/` |
| API config | `api/src/main/java/com/testing/training/config/` |

## Support & Contribution

### Support

If a scenario fails:

1. Check the WireMock stub under `runner/src/test/resources/wiremock/mappings/`.
2. Check the fixture body under `runner/src/test/resources/wiremock/__files/`.
3. Verify the selected user alias exists in `usertestdata.json`.
4. Verify `serenity.conf` points to the intended environment.

### Contribution

- Keep changes small and focused.
- Prefer reusable Tasks over logic in steps.
- Keep new scenarios deterministic and mock-backed.
- Update the README when the project flow or startup steps change.
