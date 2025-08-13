# Kế hoạch Quản lý bộ Flashcard - REVISED

## Tóm tắt mục tiêu
Phát triển thêm các tính năng còn thiếu cho **flashcard-service** hiện tại trong hệ thống Spaced Repetition modular monolith, tập trung vào những chức năng cần thiết và khả thi cho phase hiện tại.

## 📋 Phân tích hiện trạng (Current State Analysis)

### ✅ Đã có sẵn (Existing Infrastructure)
- **Modular Architecture**: 7 services độc lập (auth, user, flashcard, review, reminder, core, main)
- **Database**: MySQL với JPA/Hibernate
- **Authentication**: Account/User system đã hoàn thiện
- **Core Services**: Exception handling, Email service với i18n
- **Basic Entities**: Deck, Flashcard, ReviewLog, User đã được định nghĩa
- **Enums**: FlashcardStatus (NEW, LEARNING, REVIEW, MASTERED), ReviewResult

### 🔧 Flashcard Service hiện tại có:
- **Entities**: Deck (title, description, ownerId), Flashcard (front, back, deckId, status)
- **Controllers**: DeckController, FlashcardController (skeleton)
- **Services & Repositories**: Đã setup cơ bản

### ⚠️ Cần bổ sung (Missing Components)
- Service implementation logic
- API endpoints chi tiết
- Study session functionality
- Progress tracking integration
- Security & authorization

---

## 1. Phân tích Actors và User Stories

### 1.1 Actors (Các tác nhân) - MVP Version (No Auth)
- **User (Người dùng)**: Bất kỳ ai có thể tạo, quản lý và học flashcard (không cần đăng ký/đăng nhập)

### 1.2 User Stories - Phase 1 (Realistic & Focused)

#### Epic 1: Hoàn thiện Flashcard CRUD
**US1.1: Implement Flashcard Service Logic**
- **Là** Developer
- **Tôi muốn** hoàn thiện logic cho FlashcardService
- **Để** có thể tạo, đọc, cập nhật, xóa flashcard

**Acceptance Criteria:**
- [ ] Implement tất cả methods trong FlashcardService
- [ ] Validation: front và back không được trống
- [ ] Bất kỳ ai cũng có thể tạo/sửa/xóa flashcard
- [ ] Sử dụng existing FlashcardStatus enum

**US1.2: Complete Deck Management**
- **Là** User
- **Tôi muốn** quản lý các deck
- **Để** tổ chức flashcard theo chủ đề

**Acceptance Criteria:**
- [ ] Implement DeckService với CRUD operations
- [ ] User có thể xem tất cả deck trong hệ thống
- [ ] Validation: title không trống, maxlength 255
- [ ] Soft delete cho deck (giữ lại data)

#### Epic 2: Basic Study Session
**US2.1: Simple Study Mode**
- **Là** User
- **Tôi muốn** học flashcard theo deck
- **Để** ghi nhớ nội dung

**Acceptance Criteria:**
- [ ] API để lấy flashcard theo deck
- [ ] Tích hợp với ReviewLog service có sẵn
- [ ] Cập nhật FlashcardStatus khi học
- [ ] Không cần UI phức tạp (API first)

#### Epic 3: Integration & Basic Features  
**US3.1: Service Integration**
- **Là** System
- **Tôi muốn** tích hợp flashcard với review service
- **Để** tracking progress

**Acceptance Criteria:**
- [ ] Kết nối với ReviewLog khi user study
- [ ] Error handling với Core Exception
- [ ] API documentation cơ bản
- [ ] Lưu progress theo sessionId thay vì userId

---

## 2. Luồng nghiệp vụ chính

### 2.1 Luồng tạo và học flashcard (MVP - No Auth)
```
1. User truy cập ứng dụng (không cần đăng nhập)
2. Tạo deck mới hoặc chọn deck có sẵn
3. Thêm flashcard vào deck
4. Bắt đầu session học tập
5. Hệ thống hiển thị flashcard tuần tự
6. User đánh giá độ khó (EASY/MEDIUM/HARD/AGAIN)
7. Cập nhật trạng thái flashcard
8. Lưu kết quả học tập (theo sessionId tạm thời)
```

### 2.2 Luồng ngoại lệ (MVP - No Auth)
- **Mất kết nối**: Lưu dữ liệu tạm thời trong localStorage
- **Deck trống**: Hiển thị hướng dẫn tạo thẻ đầu tiên
- **Session expired**: Tạo session mới tự động

---

## 3. Ràng buộc nghiệp vụ

### 3.1 Ràng buộc dữ liệu (MVP - No Auth)
- Flashcard: câu hỏi và đáp án không được trống
- Deck: tên deck tối đa 100 ký tự
- Không giới hạn số deck (vì không có user management)

### 3.2 Ràng buộc bảo mật (MVP - No Auth)
- Tất cả deck đều public và có thể truy cập bởi bất kỳ ai
- Không có ownership concept
- Không có permission checking

---

## 4. Chức năng cần implement (Dựa trên existing structure)

### 4.1 Phase 1 - Core Implementation (Ưu tiên cao)

#### F1: Complete FlashcardService ✅ **REUSE existing**
**Mô tả:** Implement logic cho service đã có skeleton
**Existing:** FlashcardService, FlashcardRepository, FlashcardController

**API Endpoints cần implement (RESTful):**
```java
// FlashcardController - RESTful design
POST   /api/v1/decks/{deckId}/flashcards     // Create flashcard in deck
GET    /api/v1/flashcards/{id}              // Get flashcard by ID  
PUT    /api/v1/flashcards/{id}              // Update flashcard
DELETE /api/v1/flashcards/{id}              // Delete flashcard
GET    /api/v1/decks/{deckId}/flashcards     // Get all flashcards in deck
GET    /api/v1/flashcards                   // Get all flashcards (with pagination)
```

**Service Methods cần viết:**
```java
// FlashcardService implementation
public FlashcardDto createFlashcard(CreateFlashcardRequest request);
public FlashcardDto getFlashcardById(String id);
public FlashcardDto updateFlashcard(String id, UpdateFlashcardRequest request);
public void deleteFlashcard(String id);
public List<FlashcardDto> getFlashcardsByDeck(String deckId);
```

#### F2: Complete DeckService ✅ **REUSE existing** 
**Mô tả:** Implement logic cho DeckService
**Existing:** DeckService, DeckRepository, DeckController

**API Endpoints (RESTful):**
```java
// DeckController - RESTful design
POST   /api/v1/decks                       // Create deck
GET    /api/v1/decks                       // Get all decks (paginated)
GET    /api/v1/decks/{id}                  // Get deck by ID with details
PUT    /api/v1/decks/{id}                  // Update entire deck
PATCH  /api/v1/decks/{id}                  // Partial update deck
DELETE /api/v1/decks/{id}                  // Soft delete deck
GET    /api/v1/decks/{id}/flashcards       // Get flashcards in deck
GET    /api/v1/decks/{id}/stats            // Get deck statistics
```

#### F3: Study Session Integration ✅ **USE existing ReviewLog**
**Mô tả:** Tích hợp với review-service có sẵn
**Existing:** ReviewLog entity, ReviewLogService

**Study Session Endpoints (RESTful):**
```java
// Study session management
POST   /api/v1/decks/{deckId}/study-sessions    // Start new study session
GET    /api/v1/study-sessions/{sessionId}      // Get current study session
PUT    /api/v1/study-sessions/{sessionId}      // Update session progress
DELETE /api/v1/study-sessions/{sessionId}      // End study session

// Study actions within session
GET    /api/v1/study-sessions/{sessionId}/next-card     // Get next flashcard
POST   /api/v1/study-sessions/{sessionId}/responses     // Submit card response
GET    /api/v1/study-sessions/{sessionId}/progress      // Get session progress
```

**Integration với ReviewService:**
- Sử dụng existing ReviewLog(userId, flashcardId, result, reactionTimeMs, nextReviewDate)
- Cập nhật FlashcardStatus dựa trên ReviewResult
- Không cần tạo StudySession entity mới

#### F4: Validation Only ✅ **USE existing Core**
**Mô tả:** Basic validation (no security)
**Existing:** Core Exception

**Implementation:**
- Bỏ qua SecurityConfig
- Không có authorization checking
- Validation với Bean Validation
- Error handling với CoreException

### 4.2 Phase 2 - Enhancements (Future)

#### F5: Study Algorithms
- Spaced repetition với existing ReviewLog
- Study statistics

#### F6: Advanced Features  
- Deck sharing (thêm isPublic vào Deck)
- File upload cho media flashcards

---

## 4.3 Database Schema

Xem chi tiết database schema đầy đủ tại: **[database-schema.md](./database-schema.md)**

**Core tables cho MVP:**
- `decks` - Container cho flashcards
- `flashcards` - Thẻ học với spaced repetition data
- `study_sessions` - Quản lý session học tập  
- `review_logs` - Log chi tiết mỗi lần review
- `deck_statistics` - Thống kê và analytics
- `system_settings` - Cấu hình hệ thống

**Key features:**
- UUID primary keys cho tất cả entities
- Soft delete support
- Comprehensive indexing strategy
- Built-in analytics tables
- Spaced repetition algorithm support

---

## 5. Backlog ưu tiên - REVISED (Dựa trên existing code)

### 🚀 Sprint 1 - Core Implementation (1 week)
**T-shirt size: M** (Nhẹ hơn vì đã có infrastructure)
- [ ] **F1**: Complete FlashcardService methods - **S** (chỉ implement logic)
- [ ] **F2**: Complete DeckService methods - **S** (chỉ implement logic)  
- [ ] **F4**: Add validation only - **S** (bỏ authorization)
- [ ] Basic integration testing - **S**

### 🎯 Sprint 2 - Study Features (1 week)
**T-shirt size: S** (Sử dụng existing ReviewService)
- [ ] **F3**: Study endpoints integration - **M**
- [ ] FlashcardStatus update logic - **S**
- [ ] API documentation (Swagger) - **S**
- [ ] Error handling polish - **XS**

### ✨ Sprint 3+ - Future Enhancements (Optional)
- [ ] **F5**: Study statistics và analytics
- [ ] **F6**: Deck sharing (thêm isPublic field)
- [ ] Media upload support
- [ ] Performance optimization

---

## 6. Implementation Strategy (Step-by-step)

### 📝 Task Breakdown - Sprint 1

#### Week 1.1: FlashcardService Implementation
1. **Tạo DTOs cần thiết** (1 day)
   - CreateFlashcardRequest/Response
   - UpdateFlashcardRequest  
   - FlashcardDto

2. **Implement FlashcardService methods** (1.5 days)
   - Sử dụng existing FlashcardRepository
   - Add validation logic
   - Bỏ qua authorization

3. **Complete FlashcardController** (1 day)
   - Implement REST endpoints
   - Error handling với CoreException

#### Week 1.2: DeckService Implementation  
4. **Tạo Deck DTOs** (0.5 day)
   - CreateDeckRequest/Response
   - UpdateDeckRequest
   - DeckDto với flashcard count

5. **Implement DeckService** (1 day)
   - CRUD operations
   - Bỏ filtering
   - Soft delete logic

### 📝 Task Breakdown - Sprint 2

6. **Study Integration** (2 days)
   - Study session RESTful endpoints
   - Tích hợp với existing ReviewLogService
   - FlashcardStatus transition logic
   - Session management

7. **Testing & Documentation** (3 days)
   - Unit tests cho services
   - Integration tests
   - API documentation

---

## 6. Thiết kế sơ bộ - Sequence Diagrams

### 6.1 Study Session Flow
```plantuml
@startuml
actor User
participant Frontend
participant Backend
participant Database

User -> Frontend: Start study session
Frontend -> Backend: POST /api/v1/decks/{deckId}/study-sessions
Backend -> Database: Create session record
Backend -> Frontend: Session ID & first card
Frontend -> User: Display card (front side)

User -> Frontend: Click to reveal answer
Frontend -> User: Show back side

User -> Frontend: Rate difficulty
Frontend -> Backend: POST /api/v1/study-sessions/{sessionId}/responses
Backend -> Database: Save response & update flashcard status
Backend -> Backend: Get next card in session
Frontend -> Backend: GET /api/v1/study-sessions/{sessionId}/next-card
Backend -> Frontend: Next card or session complete
@enduml
```

### 6.2 Deck Creation Flow
```plantuml
@startuml
actor User
participant Frontend
participant Backend
participant Database

User -> Frontend: Create new deck
Frontend -> Backend: POST /api/v1/decks
Backend -> Database: Insert deck record
Backend -> Frontend: Deck created successfully

User -> Frontend: Add flashcard to deck
Frontend -> Backend: POST /api/v1/decks/{deckId}/flashcards
Backend -> Database: Insert flashcard with deck_id
Backend -> Frontend: Card added successfully
@enduml
```

---

## 7. Step-back Review - REVISED

### 7.1 Đánh giá lại dựa trên existing codebase ✅

#### ✅ Điểm mạnh của approach mới:
1. **Tận dụng infrastructure có sẵn**: Không cần tạo từ đầu
2. **Modular architecture**: Dễ maintain và extend
3. **Consistent với existing code**: Follow established patterns  
4. **Realistic timeline**: 2 weeks thay vì 4-5 weeks
5. **Low risk**: Build on proven foundation

#### ⚠️ Potential risks (đã giảm thiểu):
1. **Integration complexity**: GIẢM (sử dụng existing services)  
2. **Learning curve**: GIẢM (team đã quen với codebase)
3. **Scope creep**: KIỂM SOÁT (chỉ implement cần thiết)

### 7.2 Revised Recommendations

#### 🎯 Focus Phase 1 (2 weeks):
- **Chỉ** implement service logic, không tạo mới entities
- **Tận dụng** existing ReviewLog thay vì tạo StudySession mới
- **Sử dụng** existing Core Exception và Authentication
- **Bỏ qua** advanced features (spaced repetition algorithm, UI/UX)

#### 💡 Quick wins có thể thêm:
- [ ] **Swagger documentation** (auto-generated)
- [ ] **Health check endpoints** 
- [ ] **Basic pagination** (đã có PageResponse)
- [ ] **Soft delete** cho Deck (thêm deleted_at field)

### 7.3 Final Implementation Plan

#### ✅ Week 1 - Core Services (5 days)
```
Day 1: FlashcardService + DTOs
Day 2: FlashcardController + validation  
Day 3: DeckService + DTOs
Day 4: DeckController + authorization
Day 5: Testing + bug fixes
```

#### ✅ Week 2 - Study Integration (5 days)  
```
Day 1-2: Study endpoints + ReviewLog integration
Day 3: FlashcardStatus update logic
Day 4: Integration testing
Day 5: Documentation + deployment prep
```

---

## 8. Success Metrics - REVISED

### 📊 Technical Success (Phase 1):
- [ ] **All API endpoints working**: 100% coverage
- [ ] **Integration tests pass**: >95% success rate  
- [ ] **Response time**: <200ms average
- [ ] **Error handling**: Proper exception responses

### 📈 Business Success (Phase 1):
- [ ] **API functional**: Ready for frontend integration
- [ ] **Data consistency**: No orphaned records
- [ ] **Security**: Owner-based access control works
- [ ] **Maintainable**: Code follows existing patterns

### 🚀 Future Growth Enablers:
- Clean API design cho mobile app
- Extensible service architecture
- Performance foundation cho scaling

---

*📝 Document được tạo bởi Claude Code với phương pháp Chain-of-thought và Step-back review*
*🔄 Version 1.0 - Ngày tạo: 2025-08-12*