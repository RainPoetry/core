import org.apache.hadoop.security.UserGroupInformation;
import scala.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * User: chenchong
 * Date: 2019/3/4
 * description:
 */
public class Process {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(UserGroupInformation.getCurrentUser().getShortUserName());
		UserGroupInformation info = UserGroupInformation.createRemoteUser("10604");
		info.addCredentials(UserGroupInformation.getCurrentUser().getCredentials());
		info.doAs(new PrivilegedExceptionAction<String>() {
			@Override
			public String run() throws Exception {
				System.out.println("hello");
				return null;
			}
		});
	}
}
