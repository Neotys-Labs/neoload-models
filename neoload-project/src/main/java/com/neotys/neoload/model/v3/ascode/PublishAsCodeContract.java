package com.neotys.neoload.model.v3.ascode;

import java.nio.file.Path;

/** Publishes one custom-action family contract into a neoload-models checkout. */
public final class PublishAsCodeContract {
	private PublishAsCodeContract() {
	}

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			throw new IllegalArgumentException("Usage: PublishAsCodeContract <repository> <contract-directory>");
		}
		final PublishResult result = new AsCodeContractPublisher().publish(Path.of(args[0]), Path.of(args[1]));
		System.out.println(result.changed() ? "contract updated" : "contract already published");
	}
}
