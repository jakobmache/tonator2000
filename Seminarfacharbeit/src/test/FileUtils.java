package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils 
{
	public static int border = 10;
	public static String character = "0";
	
	public static void searchForZeros(String path) throws IOException
	{
		int count = 0;
		int lineCount = 0;
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		while (reader.readLine() != null)
		{
			lineCount++;
			String line = reader.readLine();
			if (line .equals(character))
				count++;
			if (count == border)
			{
				System.out.printf("Zeile: %d\n", lineCount);
				count = 0;
			}
		}
		reader.close();
	}
	
	public static void main(String[] args) throws IOException {
		searchForZeros("ausgabe.txt");
	}

}
