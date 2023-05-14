package com.laorencel.uilibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 分页，实现序列化Parcelable接口
 */
public class Pagination implements Parcelable {
    /**
     * 分页起始页
     */
    public static final int PAGE_START = 1;
    /**
     * 分页每页条数
     */
    public static final int PAGE_SIZE = 20;
    /**
     * 当前分页
     */
    private int page = PAGE_START;
    /**
     * 分页条数
     */
    private int pageSize = PAGE_SIZE;
    /**
     * 总条数
     */
    private int total;
    /**
     * 总页数
     */
    private int pageCount;

    public Pagination() {
    }

    public Pagination(int page) {
        this.page = page;
    }

    public Pagination(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 请求数据开始条数
     *
     * @return
     */
    public int getStart() {
        //如果是开始页（有的是0，有的是1），需要从数据库第一条开始查
        return (this.page == PAGE_START ? 0 : this.page) * pageSize;
    }

    /**
     * 请求数据结束条数
     *
     * @return
     */
    public int getEnd() {
        return ((this.page == PAGE_START ? 0 : this.page) + 1) * pageSize;
    }

    /**
     * @return 是否为第一页
     */
    public boolean isStartPage() {
        return this.page == PAGE_START;
    }

    /**
     * 减去一页
     */
    public void minusPage() {
        if (this.page > PAGE_START) {
            this.page--;
        }
    }

    /**
     * 加一页
     */
    public void plusPage() {
        this.page++;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageCount() {
        return total > 0 && pageSize > 0 ? (int) Math.ceil(total / pageSize) : 0;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pageCount=" + pageCount +
                '}';
    }

    protected Pagination(Parcel in) {
        page = in.readInt();
        pageSize = in.readInt();
        total = in.readInt();
        pageCount = in.readInt();
    }

    public static final Creator<Pagination> CREATOR = new Creator<Pagination>() {
        @Override
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        @Override
        public Pagination[] newArray(int size) {
            return new Pagination[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(pageSize);
        parcel.writeInt(total);
        parcel.writeInt(pageCount);
    }
}
