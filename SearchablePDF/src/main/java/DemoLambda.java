import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;

public class DemoLambda implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context ctx) {

        S3EventNotification.S3EventNotificationRecord record = event.getRecords().get(0);

        String bucketName = record.getS3().getBucket().getName();
        String keyName = record.getS3().getObject().getKey();
        String keyNameLower = record.getS3().getObject().getKey().toLowerCase();
        String filename = keyName;
        System.out.println("File name before replace  " + filename);
        String fullPath = filename.replaceAll("\\+", " ");
        System.out.println("File name after replace + with whitespace  " + fullPath);
        int index = fullPath.lastIndexOf("/");
        String str = fullPath.substring(index + 1);
        String Output_file_name = str.substring(0, str.lastIndexOf('.'));
        Output_file_name = Output_file_name + ".pdf";
        System.out.println("Output file name will be " + Output_file_name);
        System.out.println("Bucket Name is " + bucketName);
        System.out.println("File Path is " + fullPath);

        try {
            if (keyNameLower.endsWith("pdf")) {
            	System.out.println("Fie is PDF file");
                DemoPdfFromS3Pdf s3Pdf = new DemoPdfFromS3Pdf();
                s3Pdf.run(bucketName, keyName, Output_file_name);

            } else if (keyNameLower.endsWith("jpg") || keyNameLower.endsWith("jpeg") || keyNameLower.endsWith("png")) {
            	System.out.println("Fie is PDF Image");
            	DemoPdfFromS3Image s3Image = new DemoPdfFromS3Image();
                s3Image.run(bucketName, keyName, Output_file_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}