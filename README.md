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
 