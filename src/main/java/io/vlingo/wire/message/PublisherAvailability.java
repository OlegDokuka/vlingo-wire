// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.wire.message;

import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;
import io.vlingo.wire.node.Name;

public class PublisherAvailability {
  public static final String TypeName = "PUB";
  
  private final String host;
  private final String name;
  private final int port;
  
  public static PublisherAvailability from(final String content) {
    if (content.startsWith(TypeName)) {
      final Name name = MessagePartsBuilder.nameFrom(content);
      final AddressType type = AddressType.MAIN;
      final Address address = MessagePartsBuilder.addressFromRecord(content, type);
      return new PublisherAvailability(name.value(), address.host().name(), address.port());
    }
    return new PublisherAvailability(Name.NO_NAME, "", 0);
  }

  public PublisherAvailability(final String name, final String host, final int port) {
    this.name = name;
    this.host = host;
    this.port = port;
  }

  public boolean isValid() {
    return !name.equals(Name.NO_NAME);
  }

  public Address toAddress() {
    return Address.from(Host.of(name), port, AddressType.MAIN);
  }

  public Address toAddress(final AddressType type) {
    return Address.from(Host.of(name), port, type);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    
    builder
      .append("PUB\n")
      .append("nm=").append(name)
      .append(" addr=").append(host).append(":").append(port);
    
    return builder.toString();
  }
}
