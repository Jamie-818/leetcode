package com.so.leetcode.a355;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 355 设计推特 <br/>
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，能够看见关注人（包括自己）的最近十条推文。你的设计需要支持以下的几个功能： <br/>
 *
 * postTweet(userId, tweetId): 创建一条新的推文 <br/>
 * getNewsFeed(userId): 检索最近的十条推文。每个推文都必须是由此用户关注的人或者是用户自己发出的。推文必须按照时间顺序由最近的开始排序。 <br/>
 * follow(followerId, followeeId): 关注一个用户 <br/>
 * unfollow(followerId, followeeId): 取消关注一个用户 <br/>
 *
 * @author show
 * @since 2020/4/13 15:12
 */
public class Twitter {
    private List<UserTweets> userTweetList = new ArrayList<>();

    public Twitter() {}

    public List<UserTweets> getUserTweetList() {
        return userTweetList;
    }

    public void setUserTweetList(List<UserTweets> userTweetList) {
        this.userTweetList = userTweetList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userTweetList\":").append(userTweetList);
        sb.append('}');
        return sb.toString();
    }

    /** 推送消息 */
    public void postTweet(int userId, int tweetId) {
        Message message = new Message();
        message.setMsgId(tweetId);
        message.setPushDate(System.currentTimeMillis());
        List<UserTweets> collect =
            this.userTweetList.stream().filter(e -> e.getUserId() == userId).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            UserTweets userTweets = collect.get(0);
            userTweets.getMsgInfo().add(message);
        } else {
            UserTweets userTweets = new UserTweets();
            userTweets.setUserId(userId);
            List<Message> tweets = new ArrayList<>();
            tweets.add(message);
            userTweets.setMsgInfo(tweets);
            this.userTweetList.add(userTweets);
        }
    }

    /** 获取指定用户发布及关注的消息 */
    public List<Integer> getNewsFeed(int userId) {
        List<Message> newsFeed = new ArrayList<>();
        List<UserTweets> collect =
            this.userTweetList.stream().filter(e -> e.getUserId() == userId).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return new ArrayList<>();
        }
        UserTweets userTweets = collect.get(0);
        List<Integer> followeeIds = userTweets.getFolloweeIds();
        for (Integer followeeId : followeeIds) {
            if (followeeId == userId) {
                continue;
            }
            newsFeed.addAll(this.getFeed(followeeId));
        }
        List<Message> tweets = userTweets.getMsgInfo();
        newsFeed.addAll(tweets);
        newsFeed.sort((a, b) -> a.getPushDate() > b.getPushDate() ? -1 : 1);
        List<Integer> messageIds = new ArrayList<>();
        for (Message message : newsFeed) {
            messageIds.add(message.getMsgId());
        }
        if (newsFeed.size() > 10) {
            return messageIds.subList(0, 10);
        }
        return messageIds;
    }

    /** 获取指定用户发布的消息 */
    private List<Message> getFeed(int userId) {
        List<UserTweets> collect =
            this.userTweetList.stream().filter(e -> e.getUserId() == userId).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return new ArrayList<>();
        }
        return collect.get(0).getMsgInfo();
    }

    /** 关注 */
    public void follow(int followerId, int followeeId) {

        List<UserTweets> collect =
            this.userTweetList.stream().filter(e -> e.getUserId() == followerId).collect(Collectors.toList());
        if (collect.isEmpty()) {
            UserTweets userTweets = new UserTweets();
            List<Integer> list = new ArrayList<>();
            list.add(followeeId);
            userTweets.setFolloweeIds(list);
            userTweets.setUserId(followerId);
            this.userTweetList.add(userTweets);
        } else {
            UserTweets userTweets = collect.get(0);
            List<Integer> followeeIds = userTweets.getFolloweeIds();
            if (followeeIds.contains(followeeId)) {
                return;
            }
            followeeIds.add(followeeId);
        }
    }

    /** 取消关注 */
    public void unfollow(int followerId, int followeeId) {

        List<UserTweets> collect =
            this.userTweetList.stream().filter(e -> e.getUserId() == followerId).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            UserTweets userTweets = collect.get(0);
            List<Integer> followeeIds = userTweets.getFolloweeIds();
            if (followeeIds.contains(followeeId)) {
                followeeIds.removeIf(e -> e == followeeId);
            }
        }
    }
}

/** 用户关注及发布消息对象 */
class UserTweets {
    private int userId;
    private List<Integer> followeeIds = new ArrayList<>();
    private List<Message> msgInfo = new ArrayList<>();

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public List<Integer> getFolloweeIds() {

        return followeeIds;
    }

    public void setFolloweeIds(List<Integer> followeeIds) {

        this.followeeIds = followeeIds;
    }

    public List<Message> getMsgInfo() {

        return msgInfo;
    }

    public void setMsgInfo(List<Message> msgInfo) {
        this.msgInfo = msgInfo;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userId\":").append(userId);
        sb.append(",\"followeeIds\":").append(followeeIds);
        sb.append(",\"msgInfo\":").append(msgInfo);
        sb.append('}');
        return sb.toString();
    }
}

/**
 * 消息 对象
 */
class Message {
    private Integer msgId;
    private Long pushDate;

    public Integer getMsgId() {

        return msgId;
    }

    public void setMsgId(Integer msgId) {

        this.msgId = msgId;
    }

    public Long getPushDate() {

        return pushDate;
    }

    public void setPushDate(Long pushDate) {
        this.pushDate = pushDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"msgId\":").append(msgId);
        sb.append(",\"pushDate\":").append(pushDate);
        sb.append('}');
        return sb.toString();
    }
}
