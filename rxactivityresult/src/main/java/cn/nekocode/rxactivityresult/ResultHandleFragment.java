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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class ResultHandleFragment extends Fragment {
    public final PublishSubject<ActivityResult> resultPublisher = PublishSubject.create();
    public final BehaviorSubject<Boolean> isAttachedBehavior = BehaviorSubject.createDefault(false);

    public ResultHandleFragment() {

    }

    public PublishSubject<ActivityResult> getResultPublisher() {
        return resultPublisher;
    }

    public BehaviorSubject<Boolean> getIsAttachedBehavior() {
        return isAttachedBehavior;
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            onAttachToContext(activity);
        }
    }

    private void onAttachToContext(Context context) {
        isAttachedBehavior.onNext(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resultPublisher.onNext(new ActivityResult(requestCode, resultCode, data));
    }
}
