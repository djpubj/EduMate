# EduMate - App Similar to Google Classroom

This is an app similar to Google Classroom, named EduMate. It is connected to Firebase for backend services such as authentication and database.

## Setup Firebase for your own project

1. **Create a Firebase Project** on the [Firebase Console](https://console.firebase.google.com/).
2. **Add your Android app** to the Firebase project.
   - In the Firebase console, click **"Add App"** and select **Android**.
   - Enter your app's package name (you can find it in your `build.gradle` file).
3. **Download the `google-services.json` file** from Firebase.
   - After adding the app, Firebase will generate the `google-services.json` file.
   - Download it and place it in the `app/` directory of this project.
4. **Add Firebase dependencies**:
   - In your `app/build.gradle`, ensure you have the necessary Firebase dependencies:
     ```gradle
     implementation 'com.google.firebase:firebase-analytics:19.0.0'
     ```
5. **Build and run** the project with your Firebase credentials.

## Troubleshooting

If you face issues, refer to the Firebase documentation or feel free to raise an issue on the repository.

## Steps to find `app-release.apk`

(Download and install apk in your android phone to check app)
1. Download file.
2. Open file.
3. Then open app folder.
4. Then open release folder.
5. Then you find `app-release.apk`


## Rules paste in your firebase realtime database rule segment
```
{
  "rules": {
    ".read": true, 
    ".write": true,  
    "Users": {
      ".indexOn": ["useruid"],
      ".read": "auth != null", // Allow authenticated users to read
      ".write": "auth != null", // Allow authenticated users to write
      "enrolledClasses":{
           ".read": "auth != null", 
           ".write": "auth != null" 
      },
      "createdClasses":{
           ".read": "auth != null", 
           ".write": "auth != null" 
      }
    },
  "classrooms" : {
     ".indexOn": ["classId"],
    "classId":{
           ".read": "auth != null", 
           ".write": "auth != null" 
    },
     ".read": "auth != null", 
     ".write": "auth != null" 
    }
  }
}
```
