# WHO STEPS App [![Build Status](http://cicd.onalabs.org/api/badges/onaio/steps-app/status.svg)](http://cicd.onalabs.org/onaio/steps-app)

STEPS is an Android application developed for the World Health Organization's STEPwise approach to noncommunicable disease risk factor surveillance. The application is used to register households and household members during survey rounds and then randomly select a household member to interview. Once a household member is randomly selected for interview, the survey is launched in the [ODK Collect](https://opendatakit.org/use/collect/) application. Supported languages are English, French, Spanish, Russian, and Arabic.

![screenshot 1](screenshots/screen1.png) ![screenshot 2](screenshots/screen2.png) ![screenshot 3](screenshots/screen3.png)

For more information, see http://www.who.int/chp/steps/en/.

## Building the app

Make sure the Gradle daemon (version 2.2.1) is installed on your build machine. Build the app by running:

```
gradle assembleDebug
```

It's good practice to sign the app before you release the app. If you don't already have a certificate for signing the APK, you can create one by running:

```
cd ~/.android
keytool -genkey -v -keystore release.keystore -alias androidreleasekey -keyalg RSA -keysize 2048 -validity 10000
```

You can show the key's SHA1 fingerprint by running:

```
keytool -v -list -keystore release.keystore
```

Add the following lines to the local.properties file in the project's root directory (you might have to create it):

```
store_file=/home/[username]/.android/release.keystore
store_password=your_key_store_pw
key_alias=androidreleasekey
key_password=your_release_key_pw
```

You can now generate a signed release APK by running:

```
gradle clean
gradle assembleRelease
```

The signed APK (`build/outputs/apk/steps-app-release.apk`) should be available for distribution if the build was successful.

## Creating the Docker Image on Your Drone-CI Server

Use these commands to create a Docker image that can be used to run Drone-CI tests for this project:

```sh
git clone https://github.com/onaio/docker-builds.git
cd docker-builds/android
docker build -t onaio/android:steps-app --build-arg "androidComponents=platform-tools,android-21,extra-android-support,extra-android-m2repository,extra-google-m2repository" --build-arg "buildToolsVersion=27.0.3" .
```
