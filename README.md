# RxActivityResult
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Release](https://img.shields.io/github/release/nekocode/RxActivityResult.svg?label=Jitpack)](https://jitpack.io/#nekocode/RxActivityResult)

This is a library that can help you to receive results from `startActivityForResult()` as an Observable. You can use it in Activity or Fragment. It actullay call the `startActivityFroResult()` in a Headless-Fragment that attaching to your Activity(/Fragment). And publish the activity results in the Headless-Fragment's `onActivityResult()`.

**Supports only RxJava 2 now.**

## Usage

Example:

```
RxActivityResult.startActivityForResult(this, intent, REQUEST_CODE)
        .subscribe(new Consumer<ActivityResult>() {
            @Override
            public void accept(@NonNull ActivityResult result) throws Exception {
                if (result.isOk()) {
                    final Intent data = result.getData();
                    // DO SOME THING
                }
            }
        });
```

See the [sample](sample/src/main/java/cn/nekocode/rxactivityresult/sample/MainActivity.java) for more detail.

## Using with gradle
- Add the JitPack repository to your `build.gradle` repositories:

```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

- Add the core dependency:

```
dependencies {
    compile 'com.github.nekocode.rxactivityresult:rxactivityresult:{lastest-version}'
}
```

- (Optional) Add the below library if you need to support api 9 and later. Besides, if you already add support-v4 dependency, I will also suggest you to use this compact library, and then use the `RxActivityResultCompact` instead of the `RxActivityResult`.

```
dependencies {
    compile 'com.github.nekocode.rxactivityresult:rxactivityresult-compact:{lastest-version}'
}
```
