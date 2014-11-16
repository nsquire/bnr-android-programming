package com.nick.android.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        this.mOrigin = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public Box(Parcel in) {
        float[] data = new float[4];

        in.readFloatArray(data);
        this.mOrigin.x = data[0];
        this.mOrigin.y = data[1];

        this.mCurrent.x = data[2];
        this.mCurrent.y = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(new float[]{
                this.mCurrent.x,
                this.mCurrent.y,
                this.mOrigin.x,
                this.mOrigin.y});
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new Box(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Box[size];
        }
    };
}

