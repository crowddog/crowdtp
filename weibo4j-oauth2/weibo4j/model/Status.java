package weibo4j.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.http.Response;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class Status extends WeiboResponse implements java.io.Serializable {

	private static final long serialVersionUID = -8795691786466526420L;

	private User user = null;                            //������Ϣ
	private Date createdAt;                              //status����ʱ��
	private String id;                                   //status id
	private String mid;                                  //΢��MID
	private long idstr;                                  //�����ֶΣ�����ʹ��                     
	private String text;                                 //΢������
	private Source source;                               //΢����Դ
	private boolean favorited;                           //�Ƿ����ղ�
	private boolean truncated;
	private long inReplyToStatusId;                      //�ظ�ID
	private long inReplyToUserId;                        //�ظ���ID
	private String inReplyToScreenName;                  //�ظ����ǳ�
	private String thumbnailPic;                         //΢�������е�ͼƬ�����Ե�ַ
	private String bmiddlePic;                           //����ͼƬ
	private String originalPic;                          //ԭʼͼƬ
	private Status retweetedStatus = null;               //ת���Ĳ��ģ�����Ϊstatus���������ת������û�д��ֶ�
	private String geo;                                  //������Ϣ�����澭γ�ȣ�û��ʱ�����ش��ֶ�
	private double latitude = -1;                        //γ��
	private double longitude = -1;                       //����
	private int repostsCount;                            //ת����
	private int commentsCount;                           //������
	private String annotations;                          //Ԫ���ݣ�û��ʱ�����ش��ֶ�
	private int mlevel;
	private Visible visible;
	public Status()
	{

	}
	public Status(Response res)throws WeiboException{
		super(res);
		JSONObject json=res.asJSONObject();
		constructJson(json);
	}

	private void constructJson(JSONObject json) throws WeiboException {
		try {
			createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
			id = json.getString("id");
			mid=json.getString("mid");
			idstr = json.getLong("idstr");
			text = json.getString("text");
			if(!json.getString("source").isEmpty()){
				source = new Source(json.getString("source"));
			}
			inReplyToStatusId = getLong("in_reply_to_status_id", json);
			inReplyToUserId = getLong("in_reply_to_user_id", json);
			inReplyToScreenName=json.getString("in_reply_toS_screenName");
			favorited = getBoolean("favorited", json);
			truncated = getBoolean("truncated", json);
			thumbnailPic = json.getString("thumbnail_pic");
			bmiddlePic = json.getString("bmiddle_pic");
			originalPic = json.getString("original_pic");
			repostsCount = json.getInt("reposts_count");
			commentsCount = json.getInt("comments_count");
			annotations = json.getString("annotations");
			if(!json.isNull("user"))
				user = new User(json.getJSONObject("user"));
			if(!json.isNull("retweeted_status")){
				retweetedStatus= new Status(json.getJSONObject("retweeted_status"));
			}
			mlevel = json.getInt("mlevel");
			geo= json.getString("geo");
			if(geo!=null &&!"".equals(geo) &&!"null".equals(geo)){
				getGeoInfo(geo);
			}
			if(!json.isNull("visible")){
				visible= new Visible(json.getJSONObject("visible"));
			}
		} catch (JSONException je) {
			throw new WeiboException(je.getMessage() + ":" + json.toString(), je);
		}
	}

	private void getGeoInfo(String geo) {
		StringBuffer value= new StringBuffer();
		for(char c:geo.toCharArray()){
			if(c>45&&c<58){
				value.append(c);
			}
			if(c==44){
				if(value.length()>0){
					latitude=Double.parseDouble(value.toString());
					value.delete(0, value.length());
				}
			}
		}
		longitude=Double.parseDouble(value.toString());
	}


	public Status(JSONObject json)throws WeiboException, JSONException{
		constructJson(json);
	}
	public Status(String str) throws WeiboException, JSONException {
		// StatusStream uses this constructor
		super();
		JSONObject json = new JSONObject(str);
		constructJson(json);
	}

	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getIdstr() {
		return idstr;
	}
	public void setIdstr(long idstr) {
		this.idstr = idstr;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public boolean isFavorited() {
		return favorited;
	}
	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}
	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public String getThumbnailPic() {
		return thumbnailPic;
	}
	public void setThumbnailPic(String thumbnailPic) {
		this.thumbnailPic = thumbnailPic;
	}
	public String getBmiddlePic() {
		return bmiddlePic;
	}
	public void setBmiddlePic(String bmiddlePic) {
		this.bmiddlePic = bmiddlePic;
	}
	public String getOriginalPic() {
		return originalPic;
	}
	public void setOriginalPic(String originalPic) {
		this.originalPic = originalPic;
	}
	public Status getRetweetedStatus() {
		return retweetedStatus;
	}
	public void setRetweetedStatus(Status retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getRepostsCount() {
		return repostsCount;
	}
	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getAnnotations() {
		return annotations;
	}
	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}
	public int getMlevel() {
		return mlevel;
	}
	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}
	public Visible getVisible() {
		return visible;
	}
	public void setVisible(Visible visible) {
		this.visible = visible;
	}
	
	public boolean isTruncated() {
		return truncated;
	}
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}
	public static StatusWapper constructWapperStatus(Response res) throws WeiboException {
		JSONObject jsonStatus = res.asJSONObject(); //asJSONArray();
		JSONArray statuses = null;
		try {
			if(!jsonStatus.isNull("statuses")){				
				statuses = jsonStatus.getJSONArray("statuses");
			}
			if(!jsonStatus.isNull("reposts")){
				statuses = jsonStatus.getJSONArray("reposts");
			}
			int size = statuses.length();
			List<Status> status = new ArrayList<Status>(size);
			for (int i = 0; i < size; i++) {
				status.add(new Status(statuses.getJSONObject(i)));
			}
			long previousCursor = jsonStatus.getLong("previous_curosr");
			long nextCursor = jsonStatus.getLong("next_cursor");
			long totalNumber = jsonStatus.getLong("total_number");
			String hasvisible = jsonStatus.getString("hasvisible");
			return new StatusWapper(status, previousCursor, nextCursor,totalNumber,hasvisible);
		} catch (JSONException jsone) {
			throw new WeiboException(jsone);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Status [user=" + user + ", idstr=" + idstr + ", createdAt="
				+ createdAt + ", id=" + id + ", text=" + text + ", source="
				+ source + ", favorited=" + favorited + ", truncated="
				+ truncated + ", inReplyToStatusId=" + inReplyToStatusId
				+ ", inReplyToUserId=" + inReplyToUserId
				+ ", inReplyToScreenName=" + inReplyToScreenName
				+ ", thumbnailPic=" + thumbnailPic + ", bmiddlePic="
				+ bmiddlePic + ", originalPic=" + originalPic
				+ ", retweetedStatus=" + retweetedStatus + ", geo=" + geo
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", repostsCount=" + repostsCount + ", commentsCount="
				+ commentsCount + ", mid=" + mid + ", annotations="
				+ annotations + ", mlevel=" + mlevel
				+ ", visible=" + visible + "]";
	}

}
