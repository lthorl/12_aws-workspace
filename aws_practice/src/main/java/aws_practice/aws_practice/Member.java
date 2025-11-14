package aws_practice.aws_practice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
	
	@Id @GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, name = "USER_ID")
	private String userId;
	
	@Column(nullable = false, name = "USER_PWD")
	private String userPwd;
	
	private String imageUrl;
	
	public Member(String name, String userId, String userPwd, String imageUrl) {
		this.name = name;
		this.userId = userId;
		this.userPwd = userPwd;
		this.imageUrl = imageUrl;
	}
}
