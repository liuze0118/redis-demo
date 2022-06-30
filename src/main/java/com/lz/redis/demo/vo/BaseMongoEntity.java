package com.lz.redis.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : liuze
 * @date: 2022/6/29 18:17
 **/
public class BaseMongoEntity implements Serializable {
    public static final String FIELD_ID = "_id";
    public static final String FIELD_VALIDSTATUS = "validStatus";
    public static final String FIELD_VERSION = "version";
    public static final String FIELD_UPDATETIME = "updateTime";
    public static final String FIELD_CREATETIME = "createTime";
    @Transient
    Date date = new Date();
    @Id
    private ObjectId id;
    private String validStatus;
    @Version
    private Long version;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;

    public Date getDate() {
        return this.date;
    }

    public ObjectId getId() {
        return this.id;
    }

    public String getValidStatus() {
        return this.validStatus;
    }

    public Long getVersion() {
        return this.version;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseMongoEntity)) {
            return false;
        } else {
            BaseMongoEntity other = (BaseMongoEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$date = this.getDate();
                Object other$date = other.getDate();
                if (this$date == null) {
                    if (other$date != null) {
                        return false;
                    }
                } else if (!this$date.equals(other$date)) {
                    return false;
                }

                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                Object this$validStatus = this.getValidStatus();
                Object other$validStatus = other.getValidStatus();
                if (this$validStatus == null) {
                    if (other$validStatus != null) {
                        return false;
                    }
                } else if (!this$validStatus.equals(other$validStatus)) {
                    return false;
                }

                label62: {
                    Object this$version = this.getVersion();
                    Object other$version = other.getVersion();
                    if (this$version == null) {
                        if (other$version == null) {
                            break label62;
                        }
                    } else if (this$version.equals(other$version)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$updateTime = this.getUpdateTime();
                    Object other$updateTime = other.getUpdateTime();
                    if (this$updateTime == null) {
                        if (other$updateTime == null) {
                            break label55;
                        }
                    } else if (this$updateTime.equals(other$updateTime)) {
                        break label55;
                    }

                    return false;
                }

                Object this$createTime = this.getCreateTime();
                Object other$createTime = other.getCreateTime();
                if (this$createTime == null) {
                    if (other$createTime != null) {
                        return false;
                    }
                } else if (!this$createTime.equals(other$createTime)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseMongoEntity;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : $date.hashCode());
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $validStatus = this.getValidStatus();
        result = result * 59 + ($validStatus == null ? 43 : $validStatus.hashCode());
        Object $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BaseMongoEntity(date=" + this.getDate() + ", id=" + this.getId() + ", validStatus=" + this.getValidStatus() + ", version=" + this.getVersion() + ", updateTime=" + this.getUpdateTime() + ", createTime=" + this.getCreateTime() + ")";
    }

    public BaseMongoEntity(Date date, ObjectId id, String validStatus, Long version, Date updateTime, Date createTime) {
        this.validStatus ="1";
        this.updateTime = this.date;
        this.createTime = this.date;
        this.date = date;
        this.id = id;
        this.validStatus = validStatus;
        this.version = version;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public BaseMongoEntity() {
        this.validStatus = "1";
        this.updateTime = this.date;
        this.createTime = this.date;
    }
}
