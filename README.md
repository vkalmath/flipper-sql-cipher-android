# flipper-sql-cipher-android
Android Flipper Plugin for Cipher SQL database.

Usage:
1. Clone the repo
2. copy flippersqlcipher to your project and add it as library project
3. On your app's build.gradle file declare the library as:
    ```
    debugImplementation project(":flippersqlcipher")
    ```

4. Configure Flipper as mentioned in flipper documentations. Add SqlCipherDatabasesFlipperPlugin like below. pass DB file name and password as as shown below.
```
    SoLoader.init(this, false)

     if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
        val client = AndroidFlipperClient.getInstance(applicationContext)
         client.addPlugin(SqlCipherDatabasesFlipperPlugin(applicationContext, "DB_ENCRYPTION_PWD", listOf("DB_NAMES")))
         client.start()
     }
```
5. This needs javascript plugin which is available at
https://github.com/vkalmath/SqlCipherflipperPlugin
