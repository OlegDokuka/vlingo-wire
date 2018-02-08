// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.wire.channel;

import java.io.IOException;

import io.vlingo.wire.message.AbstractMessageTool;
import io.vlingo.wire.message.RawMessage;

public class MockChannelReader extends AbstractMessageTool implements ChannelReader {
  public static final String MessagePrefix = "Message-";
  
  public int probeChannelCount;

  private ChannelReaderConsumer consumer;
  
  public MockChannelReader() {
  }
  
  @Override
  public void close() {

  }

  @Override
  public String name() {
    return "mock";
  }

  @Override
  public void openFor(final ChannelReaderConsumer consumer) throws IOException {
    this.consumer = consumer;
  }

  @Override
  public void probeChannel() {
    ++probeChannelCount;
    
    final RawMessage message = RawMessage.from(0, 0, MessagePrefix + probeChannelCount);
    
    consumer.consume(message);
  }
}
