package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import classes.Person;

public class FileHelper<T extends Person<T>> {

	private final File file;

	/** An instance of the class being managed by this FileIO manager. */
	private final T classType;

	/**
	 * Populates the list of people using stored data, if it exists.
	 *
	 * @param dir
	 *            The file directory.
	 * @param fileName
	 *            The file name.
	 * @param classType
	 *            The type of Object to create/manage.
	 * @throws IOException
	 *             Thrown if an error occurs with the File.
	 */
	public FileHelper(File dir, String fileName, T classType)
			throws IOException {
		this.file = new File(dir, fileName);
		this.classType = classType;

		// Read the file if it exists, or create a new one if it doesn't
		if (file.exists())
			read();
		else
			file.createNewFile();
	}

	/**
	 * Reads the CSV file, creating the objects and adding them to the people
	 * list.
	 *
	 * @throws IOException
	 *             Thrown if there is an error with reading the file.
	 */
	public void read() throws IOException {
		FileInputStream fis = new FileInputStream(this.file);
		Scanner in = new Scanner(fis);

		// Parse each line: instantiate the user, and add it to the list
		while (in.hasNextLine())
			classType.scan(in.nextLine().split(", "));

		fis.close();
		in.close();
	}

	/**
	 * Saves the file with the contents of people. If append is true, appends to
	 * the file, otherwise overwrites the file's contents.
	 *
	 * @param people
	 *            The list of people to write.
	 * @param append
	 *            Append to the file if true, otherwise overwrite it.
	 * @throws IOException
	 *             Thrown if there is an error while writing.
	 */
	public void save(List<T> people, boolean append) throws IOException {
		FileOutputStream out = new FileOutputStream(this.file, append);

		// Write the CSV formatted content to the file, 1 person per line
		for (T t : people)
			out.write((t + "\n").getBytes());

		out.flush();
		out.close();
	}
}
