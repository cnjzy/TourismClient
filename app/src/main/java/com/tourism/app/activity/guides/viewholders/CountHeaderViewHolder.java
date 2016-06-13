/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tourism.app.activity.guides.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;

/**
 * Created by tomas on 15/07/15.
 */
public class CountHeaderViewHolder extends RecyclerView.ViewHolder {

    public View item_title_ll;
    public TextView item_title_date_name_tv;
    public TextView item_title_date_tv;
    public View contenet_ll;
    public ImageView item_icon_iv;
    public TextView item_name_tv;

    public CountHeaderViewHolder(View itemView) {
        super(itemView);

        item_title_ll = itemView.findViewById(R.id.item_title_ll);
        item_title_date_name_tv = (TextView) itemView.findViewById(R.id.item_title_date_name_tv);
        item_title_date_tv = (TextView) itemView.findViewById(R.id.item_title_date_tv);
        contenet_ll = itemView.findViewById(R.id.contenet_ll);
        item_icon_iv = (ImageView) itemView.findViewById(R.id.item_icon_iv);
        item_name_tv = (TextView) itemView.findViewById(R.id.item_name_tv);
    }

}
