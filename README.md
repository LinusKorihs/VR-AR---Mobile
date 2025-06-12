The project:

A Android-Application that pulls random user data over a web API and saves them locally in a data bank. The user can create QR-Codes of the contacts and scan those to get the information of the random user.

To run it:

(Needed)
- Have Android Studio installed
- Gradle over the wrapper
- Has API Level of 35 for Android
- JDK, Kotlin, Ktor and other librarys for the certain version (if not they will be downloaded by syncing with the gradle files)

1. Open project in Android Studio
2. Gradle sync
3. Open the App over an Android with API/SDK 25 or higher. If not use a emulator with the correct API version
4. Make sure the Android has the rights to use the camera. If not go to the device in device manager -> edit -> additional settings -> camera and choose for all possible cams the webcam. Click on finish. Restart the emulator and now the device should be able to use the computer camera

App Features:

- Show Contacs
- Show Contact details like profile picture, name or address
- Generate QR Codes with the contact information
- Scan QR Codes to obtain contact information
- Clear data bank
- Import new contacts over the api
- Export the contacts local
