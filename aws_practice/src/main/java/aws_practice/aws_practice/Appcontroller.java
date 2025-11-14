package aws_practice.aws_practice;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

@RestController
public class Appcontroller {
	
	private MemberRepository memberRepository;
	private S3Operations s3Operations;
	
	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucketName;
	
	public Appcontroller(
		MemberRepository memberRepository,
		S3Operations s3Operations
	){
		this.memberRepository = memberRepository;
		this.s3Operations = s3Operations;
	}
	
	@GetMapping("health")
	public ResponseEntity<String> healthCheck(){
		return ResponseEntity.ok().body("Success Health Check");
	}
	
	@GetMapping("members")
	public ResponseEntity<List<Member>> getMembers(){
		List<Member> members = memberRepository.findAll();
		return ResponseEntity.ok().body(members);
	}
	
	@PostMapping("members")
	public ResponseEntity<Member> createMember(
			@RequestPart("image")MultipartFile imageFile,
			@RequestPart("name") String name,
			@RequestPart("userId") String userId,
			@RequestPart("userPwd") String userPwd
			){
		String originalFilename = imageFile.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = Instant.now().getEpochSecond() + fileExtension;
		
		String imageUrl;
		try(InputStream inputStream = imageFile.getInputStream()){
			S3Resource s3Resource = s3Operations.upload(bucketName, filename, inputStream,
			ObjectMetadata.builder().contentType(imageFile.getContentType()).build());
			imageUrl = s3Resource.getURL().toString();
		} catch (IOException e){
			throw new RuntimeException(e);
		}
		
		Member member = new Member(name,userId,userPwd,imageUrl);
		memberRepository.save(member);
		return ResponseEntity.ok().body(member);
	}
}

