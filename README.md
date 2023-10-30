# COMP90018-AI-ChatBot
# Git Commit Message Standard
`Commit Type`
```
- [Feat]      |   What's New
- [Add]       |   Create a capability e.g. feature, test, dependency
- [Cut]       |   Remove a capability e.g. feature, test, dependency
- [Fix]       |   bug fixes
- [Perf]      |   Performance optimisation
- [Style]     |   Code formatting
- [Test]      |   Test case additions or updates
- [Ci]        |   Deployment-related file changes
- [Docs]      |   Changes to document classes
- [Chore]     |   Other types
- [Refactor]  |   Code simplification, removal of redundant code, etc
```

## Backend Debug Mode
flask --app Backend run --host=0.0.0.0
flask --app IMServer.src.app run
[NEW]: run `docker compose up` to auto reload the projects 

## Actural deployment (Production mode)
1. install docker 
2. build docker image `docker build -t chat-backend .`
3. run container `docker run --net=host chat-backend`
4. using nginx do reverse proxy to handle gunicorn server
   
# API Doc
https://documenter.getpostman.com/view/17696381/2s9YC1XEqp

# Useful Document

- http request lib: https://square.github.io/retrofit/
- android material: https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md
# Important Notes for android development
  
1. Zhihao have refactored the project, everyone must pay close attention to the project structure and naming conventions.
2. Refer to LoginManager and SharedPreferencesHelper to store some global data.
3. Refer to LoginActivity - Add loading animations to some buttons using this library: https://github.com/leandroBorgesFerreira/LoadingButtonAndroid#installation
4. You can look at LoginActivity, SignActivity, and MessageActivity -> handleSendMessage, and the classes in the network package to send network requests. We are using a package called retrofit2.
5. Refer to MessageActivity's onOptionsItemSelected and setupActionBar to use the Actionbar to display the top title and action bar.
6. Pay attention to maintaining a visually pleasing distance between the view elements.

# Trello Link
https://trello.com/w/chatbot723

# Project structure (Android)
 
## Directory Structure

```
AiChatBot
└───main
    └───java
        └───com
            └───unimelb
                └───aichatbot
                    ├───modules
                    │   ├───account
                    │   │   ├───activity
                    │   │   └───service
                    │   ├───chat
                    │   │   ├───activity
                    │   │   ├───adapter
                    │   │   ├───model
                    │   │   │   └───type
                    │   │   └───service
                    │   ├───chatHistory
                    │   │   ├───adapter
                    │   │   ├───fragment
                    │   │   └───placeholder
                    │   ├───contacts
                    │   │   └───fragment
                    │   ├───profile
                    │   │   └───activity
                    │   ├───searchFriend
                    │   │   ├───fragment
                    │   │   └───model
                    │   └───setting
                    │       └───fragment
                    ├───network
                    └───util
```

## Module Descriptions

### `account`

- `activity`: Contains activities related to user accounts such as login, registration, etc.
- `service`: Holds services responsible for account operations.

### `chat`

- `activity`: Contains the main chat interface activities.
- `adapter`: Holds adapters used for RecyclerViews in the chat module.
- `model`: Contains data models related to chats.
  - `type`: Defines different types of chat messages.
- `service`: Http Services for handling chat functionalities.

### `chatHistory`

- `adapter`: Adapter for the chat history list.
- `fragment`: Fragments displaying chat history.
- `placeholder`: Placeholder elements for loading or empty states.

### `contacts`

- `fragment`: Fragments for the contacts screen.

### `profile`

- `activity`: Activities for user profile screens.

### `searchFriend`

- `fragment`: Fragments for the friend search interface.
- `model`: Data models used in friend search functionalities.

### `setting`

- `fragment`: Fragments related to application settings.

### `network`

- Contains classes related to network operations.

### `util`

- Utility classes used across the project.

---
## Program flowchart (Android)
 
``` mermaid
graph TD

A[Start App] --> B[Initialize Components]
B --> C{User Signed In?}

C -->|Yes| D[Chat History Page]
C -->|No| E[Show Sign-in/Register Page]

D --> D1[Display Chat History & Rooms]
D -->|Bottom Nav| D2[Friends Page]
D -->|Bottom Nav| D3[Search Friend Page]
D -->|Bottom Nav| D4[Settings Page]

 
D1 -->|User Clicks a Chat/Room| G[Enter Chat/Room]
 

D2 --> H{Tap a Friend?}
 
H -->|Short tap| G
 
H -->|Long tap| IA[Show Operations Menu]


IA --> IB{Select Operation}
IB -->|View Profile| IC[View Friend Profile]
IB -->|Delete| ID[Delete Friend]
IC --> ICD[Done]
ID --> ICD
ICD --> D2
D3 --> K[Display Search Bar]
K --> L{Search Friend?}
L -->|Yes| M[Show Search Results]
L -->|No| K

M --> N{Select a Friend in Results?}
N -->|Yes| G
N -->|No| M

D4 --> P[Display Settings]
P --> Q{Select Option?}
Q -->|Profile| R[Go to Profile Page]
Q -->|Logout| S[Log Out]
 

S --> A[Start App]

E --> S1[User Sign-in]
E --> S2[User Register]

S1 --> T[Redirect to Chat History]
S2 --> U[Registration Process]
U --> V[Redirect to Chat History]

T --> D
V --> D

G --> W[Load Messages]
W --> X[Display Messages]
X --> Y{New Message?}
Y -->|Yes| Z[Add to Chat]
Y -->|No| X

Z --> AA[Send Message]
AA --> AB[Update Message List]
AB --> X

R --> AC[Display Profile]
AC --> AD{Update Name/Avatar?}
AD -->|Yes| AE[Save Changes]
AD -->|No| AC
AE --> AF[Update Profile]
AF --> AC
 
D1 -->|Tap Create New Chat Button| FA[Search AI Bot Page]

 
 

FA --> FB[Select an AI Bot]
FB --> FC[Start New Chat with AI Bot]
FC --> G

```