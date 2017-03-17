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

package cn.nekocode.rxactivityresult;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class RxActivityResult {
    private static final String FRAGMENT_TAG = "_RESULT_HANDLE_FRAGMENT_";

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static Observable<ActivityResult> startActivityFroResult(
            @NonNull Activity activity, @NonNull Intent intent, int requestCode) {

        return startActivityFroResult(activity.getFragmentManager(), intent, requestCode, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Observable<ActivityResult> startActivityFroResult(
            @NonNull Activity activity, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {

        return startActivityFroResult(activity.getFragmentManager(), intent, requestCode, options);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static Observable<ActivityResult> startActivityFroResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {

        return startActivityFroResult(fragment.getFragmentManager(), intent, requestCode, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Observable<ActivityResult> startActivityFroResult(
            @NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @NonNull Bundle options) {

        return startActivityFroResult(fragment.getFragmentManager(), intent, requestCode, options);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private static Observable<ActivityResult> startActivityFroResult(
            @NonNull FragmentManager fragmentManager, @NonNull final Intent intent, final int requestCode, @Nullable final Bundle options) {

        ResultHandleFragment _fragment = (ResultHandleFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (_fragment == null) {
            _fragment = new ResultHandleFragment();

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(_fragment, FRAGMENT_TAG);
            transaction.commit();

        } else if (Build.VERSION.SDK_INT >= 13 && _fragment.isDetached()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.attach(_fragment);
            transaction.commit();
        }

        final ResultHandleFragment fragment = _fragment;
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
                        if (Build.VERSION.SDK_INT >= 16) {
                            fragment.startActivityForResult(intent, requestCode, options);
                        } else {
                            fragment.startActivityForResult(intent, requestCode);
                        }

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
