/*
 * Copyright 2017 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.recyclerview.swipe.sample.activity.nested;

import java.util.Arrays;
import java.util.List;

import com.yanzhenjie.recyclerview.swipe.sample.R;
import com.yanzhenjie.recyclerview.swipe.sample.activity.BaseActivity;

import android.content.Intent;
import android.view.View;

/**
 * <p>
 * 几种嵌套使用的演示。
 * </p>
 * Created by YanZhenjie on 2017/7/21.
 */
public class NestedActivity extends BaseActivity {

    @Override
    public void onItemClick(View itemView, int position) {
        switch (position) {
            case 0: {
                startActivity(new Intent(this, CardViewActivity.class));
                break;
            }
            case 1: {
                startActivity(new Intent(this, DrawerActivity.class));
                break;
            }
            case 2: {
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
            }
        }
    }

    @Override
    protected List<String> getItemList() {
        return Arrays.asList(getResources().getStringArray(R.array.nested_item));
    }
}
