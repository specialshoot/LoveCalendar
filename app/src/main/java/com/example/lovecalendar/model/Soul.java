package com.example.lovecalendar.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by 轾 on 2015/10/10.
 */
public class Soul implements Serializable {

    @PrimaryKey(PrimaryKey.AssignType.AUTO_INCREMENT) @Column("_id") protected long id;
}
