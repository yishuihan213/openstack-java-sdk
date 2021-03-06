package org.openstack.client.cli.commands;

import java.io.InputStream;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.openstack.client.cli.model.StoragePath;
import org.openstack.client.storage.ObjectsResource;
import org.openstack.client.storage.OpenstackStorageClient;
import org.openstack.model.storage.ObjectProperties;
import org.openstack.utils.NoCloseInputStream;

public class UploadFile extends OpenstackCliCommandRunnerBase {
	@Argument(index = 0)
	public StoragePath path;

	@Argument(index = 1, multiValued = true)
	public List<String> properties;

	public UploadFile() {
		super("upload", "file");
	}

	@Override
	public Object runCommand() throws Exception {
		OpenstackStorageClient client = getStorageClient();

		String[] tokens = path.getKey().split("/");
		if (tokens.length != 2) {
			throw new IllegalArgumentException("Cannot parse: " + path.getKey());
		}
		ObjectsResource objects = client.root().containers().id(tokens[0]).objects();

		ObjectProperties objectProperties = new ObjectProperties();
		objectProperties.setName(tokens[1]);

		if (properties != null) {
			for (String property : properties) {
				int equalsIndex = property.indexOf('=');
				if (equalsIndex == -1) {
					throw new IllegalArgumentException("Can't parse: " + property);
				}

				String key = property.substring(0, equalsIndex);
				String value = property.substring(equalsIndex + 1);

				objectProperties.getCustomProperties().put(key, value);
			}
		}

		// This command will probably be faster _not_ in nailgun mode
		InputStream stream = new NoCloseInputStream(System.in);
		objects.putObject(stream, -1, objectProperties);

		return path.getKey();
	}
}
