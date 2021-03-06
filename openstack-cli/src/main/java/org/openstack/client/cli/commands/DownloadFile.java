package org.openstack.client.cli.commands;

import java.io.InputStream;

import org.kohsuke.args4j.Argument;
import org.openstack.client.cli.model.StoragePath;
import org.openstack.client.storage.OpenstackStorageClient;
import org.openstack.utils.Io;

public class DownloadFile extends OpenstackCliCommandRunnerBase {
	@Argument(index = 0)
	public StoragePath path;

	public DownloadFile() {
		super("download", "file");
	}

	@Override
	public Object runCommand() throws Exception {
		OpenstackStorageClient client = getStorageClient();

		InputStream is = path.getResource(client).openStream();
		try {
			Io.copyStreams(is, System.out);
		} finally {
			Io.safeClose(is);
		}

		return null;
	}

}
