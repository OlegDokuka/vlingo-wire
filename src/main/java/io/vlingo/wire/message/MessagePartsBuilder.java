// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.wire.message;

import java.util.HashSet;
import java.util.Set;

import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class MessagePartsBuilder {
  public static final Address addressFromRecord(final String record, final AddressType type) {
    final String text = parseField(record, type.field());

    if (text == null) {
      return Address.NO_NODE_ADDRESS;
    }

    return Address.from(text, type);
  }

  public static final Set<Address> addressesFromRecord(final String record, final AddressType type) {
    final Set<Address> addresses = new HashSet<>();

    final String[] parts = record.split("\n");

    if (parts.length < 3) {
      return addresses;
    }

    for (int index = 2; index < parts.length; ++index) {
      addresses.add(addressFromRecord(parts[index], type));
    }

    return addresses;
  }

  public static final Set<Node> nodesFrom(String content) {
    final Set<Node> nodeEntries = new HashSet<Node>();

    final String[] parts = content.split("\n");

    if (parts.length < 3) {
      return nodeEntries;
    }

    for (int index = 2; index < parts.length; ++index) {
      nodeEntries.add(nodeFromRecord(parts[index]));
    }

    return nodeEntries;
  }

  public static final Node nodeFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Node.NO_NODE;
    }

    return nodeFromRecord(parts[1]);
  }

  public static final Node nodeFromRecord(final String record) {
    final Id id = idFromRecord(record);
    final Name name = nameFromRecord(record);
    final Address opAddress = addressFromRecord(record, AddressType.OP);
    final Address appAddress = addressFromRecord(record, AddressType.APP);

    return new Node(id, name, opAddress, appAddress);
  }

  public static final Id idFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Id.NO_ID;
    }

    return idFromRecord(parts[1]);
  }

  public static final Id idFromRecord(final String record) {
    final String text = parseField(record, "id=");

    if (text == null) {
      return Id.NO_ID;
    }

    return Id.of(Short.parseShort(text));
  }

  public static final Name nameFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 2) {
      return Name.NO_NODE_NAME;
    }

    return nameFromRecord(parts[1]);
  }

  public static final Name nameFromRecord(final String record) {
    final String text = parseField(record, "nm=");

    if (text == null) {
      return Name.NO_NODE_NAME;
    }

    return new Name(text);
  }

  public static final String parseField(final String record, final String fieldName) {
    final String skinnyRecord = record.trim();

    final int idIndex = skinnyRecord.indexOf(fieldName);

    if (idIndex == -1) {
      return null;
    }

    final int valueIndex = idIndex + fieldName.length();
    final int space = skinnyRecord.indexOf(' ', valueIndex);

    if (valueIndex >= space) {
      return skinnyRecord.substring(valueIndex);
    }

    return skinnyRecord.substring(valueIndex, space);
  }
}
