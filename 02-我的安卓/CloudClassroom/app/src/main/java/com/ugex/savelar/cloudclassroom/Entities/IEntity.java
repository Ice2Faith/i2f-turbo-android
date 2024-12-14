package com.ugex.savelar.cloudclassroom.Entities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

public interface IEntity {
    boolean updateToDb(ContentResolver cr);
    boolean hasContiansInDb(ContentResolver cr);
    boolean deleteFromDb(ContentResolver cr);
    ContentValues toValues();
    boolean getDataFromDb(ContentResolver cr);
}
