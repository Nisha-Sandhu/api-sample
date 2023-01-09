package com.example.wisdomleafassignment.base;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef ({ Status.FAILURE, Status.SUCCESS, Status.LOADING})
public @interface Status {
    int FAILURE = 0;
    int SUCCESS = 1;
    int LOADING = 2;

}