package com.wjy.secondkill.vo;

import com.wjy.secondkill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 爱吃羊的大灰狼
 * @date: 2022/2/15 15:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecKillMessage {

    private User user;

    private Long goodId;
}
