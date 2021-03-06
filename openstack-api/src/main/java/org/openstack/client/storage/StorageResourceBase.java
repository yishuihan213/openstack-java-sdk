package org.openstack.client.storage;

import javax.ws.rs.core.MediaType;

import org.openstack.client.common.Resource;

abstract class StorageResourceBase extends Resource {
	public StorageResourceBase() {
	}

	@Override
	protected MediaType getDefaultContentType() {
		return MediaType.APPLICATION_XML_TYPE;
	}
}
