/*
 * Copyright 2017. nekocode (nekocode.cn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.nekocode.rxactivityresult.compact;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cn.nekocode.rxactivityresult.ActivityResult;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class RxActivityResultCompact {
    private static final String FRAGMENT_TAG = "_RESULT_HANDLE_FRAGMENT_";

    public static Observable<ActivityResult> startActivityForResult(
            @NonNull AppCompatActivity activity, @NonNull Intent intent, int requestCode) {

        return startActivityForResult(activity.getSupportFragmentManager(), intent, requestCode, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull AppCompatActivity activity, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {

        return startActivityForResult(activity.getSupportFragmentManager(), intent, requestCode, options);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {

        return startActivityForResult(fragment.getFragmentManager(), intent, requestCode, null);
    }

    public static Observable<ActivityResult> startActivityForResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @NonNull Bundle options) {

        return startActivityForResult(fragment.getFragmentManager(), intent, requestCode, options);
    }

    private static Observable<ActivityResult> startActivityForResult(
            @NonNull FragmentManager fragmentManager, @NonNull final Intent intent, final int requestCode, @Nullable final Bundle options) {

        ResultHandleV4Fragment _fragment = (ResultHandleV4Fragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (_fragment == null) {
            _fragment = new ResultHandleV4Fragment();

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(_fragment, FRAGMENT_TAG);
            transaction.commit();

        } else if (_fragment.isDetached()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.attach(_fragment);
            transaction.commit();
        }

        final ResultHandleV4Fragment fragment = _fragment;
        return fragment.getIsAttachedBehavior()
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull Boolean isAttached) throws Exception {
                        return isAttached;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<ActivityResult>>() {
                    @Override
                    public ObservableSource<ActivityResult> apply(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        fragment.startActivityForResult(intent, requestCode, options);
                        return fragment.getResultPublisher();
                    }
                })
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull ActivityResult result) throws Exception {
                        return result.getRequestCode() == requestCode;
                    }
                });
    }
}
