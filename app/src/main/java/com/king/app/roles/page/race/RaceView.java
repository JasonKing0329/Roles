package com.king.app.roles.page.race;

import com.king.app.roles.base.BaseView;
import com.king.app.roles.model.entity.Race;

import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:42
 */

public interface RaceView extends BaseView {
    void showRaces(List<Race> races);
}
