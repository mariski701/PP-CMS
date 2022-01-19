package com.cms.pp.cms.pp.Comment;

import lombok.ToString;

@ToString
public class CommentsCountModel {
    private int userId;
    private long total;

    public int getCommentId() {
        return userId;
    }

    public void setCommentId(int userId) {
        this.userId = userId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public CommentsCountModel(int userId, long total) {
        this.userId = userId;
        this.total = total;
    }
}
