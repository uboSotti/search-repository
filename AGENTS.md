# PROJECT CONTEXT & ARCHITECTURE
- Project Type: Android Multi-module Project.
- Primary Stack: Kotlin, Jetpack Compose, Coroutines, Flow.
- DI Framework: Hilt (Dagger-Hilt).
- Architecture Foundation: 모든 레이어의 정의와 역할 분담은 Clean Architecture 원칙을 근간으로 한다.
- Architecture Reference:
    1. https://developer.android.com/topic/architecture.md.txt
    2. https://developer.android.com/topic/architecture/recommendations.md.txt

# MULTI-MODULE & LAYER GUIDELINES (CLEAN ARCHITECTURE)
- Layer Definitions:
    - Domain Layer: 비즈니스 로직과 Entity 를 포함하는 최상위 레이어 (Any-module independent).
    - Data Layer: Repository 구현체와 외부 데이터 소스(Network, Local DB)를 관리.
    - Feature/UI Layer: 사용자와 상호작용하며 UI 상태(State)와 ViewModel 을 관리.
- Dependency Flow: 의존성은 항상 안쪽(Domain)으로 향해야 하며, Domain 레이어는 다른 레이어에 의존하지 않는다.
- Module Graph Verification: 모듈이 추가되거나 변경될 때마다, Clean Architecture 의 의존성 규칙에 따라 그래프가 올바르게 연결되어 있는지 반드시 확인한다.

# TECHNICAL STANDARDS
- Asynchronous Processing: 모든 비동기 처리는 가급적 **Coroutines **를 활용하여 구현하며, 필요에 따라 Flow 를 병행한다.
- Compose UI Optimization: **Compose ** UI 작성 시 **remember**, **derivedStateOf**, **Stable**, **Immutable ** 등의 최적화 기법을 적극 활용하여 **Recomposition **을 최소화하는 전략을 강구한다.
- State Management: UI 상태는 ViewModel 내에서 StateFlow 를 통해 관리하며, 불변성(Immutability)을 유지한다.

# DEPENDENCY INJECTION (HILT) RULES
- Hilt Modules: 각 모듈의 의존성은 해당 모듈 내부에 정의된 Hilt Module (@Module, @InstallIn)에서 관리한다.
- Entry Points: @AndroidEntryPoint 사용 시 해당 컴포넌트가 속한 모듈의 의존성 그래프가 올바른지 사전에 검토한다.

# CODING & DOCUMENTATION STANDARDS
- Documentation:
    - Interface 를 생성하거나 변경할 때는 반드시 해당 인터페이스의 역할과 목적을 기술하는 주석을 작성한다.
    - 복잡한 비즈니스 로직이나 알고리즘이 포함된 경우에만 이해를 돕기 위한 주석을 추가한다.
    - 단순 UI 코드나 자명한 데이터 클래스에는 주석을 생략한다.

# AGENT VERIFICATION CHECKLIST
1. Build Success: 제안된 코드가 현재 멀티 모듈 환경에서 빌드 에러를 유발하지 않는가?
2. Clean Architecture: 레이어 간의 역할 분담이 클린 아키텍처 원칙을 준수하고 있는가?
3. Optimization: **Compose ** 작성 시 불필요한 **Recomposition **이 발생하지 않도록 최적화가 고려되었는가?
4. Async Strategy: 비동기 로직이 **Coroutines **를 기반으로 적절한 Scope 내에서 실행되는가?