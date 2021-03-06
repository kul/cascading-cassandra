package com.ifesdjeen.cascading.cassandra.sinks;

import cascading.tuple.TupleEntry;
import org.apache.cassandra.thrift.*;
import org.apache.hadoop.mapred.OutputCollector;

import java.io.IOException;
import java.util.*;

import java.nio.ByteBuffer;

/**
 * ISink is used to allow flexibility when making different types of output for Cassandra.
 * Since data model is rich and there're several ways to interpret the way tuples are stored
 * in C*, ISink provides a common way to write tuples.
  */
public interface IConfigurableSink {

  /**
   * configure the sink : called once afer sink construction
   *
   * @param settings - settings object passed while constructing CassandraScheme
   *
   */
  public void configure(Map<String, Object> settings);

  /**
   * Create mutation for given `tupleEntry`
   *
   * @param tupleEntry - cascading TupleEntry to be persisted
   * @return - mutation to be applied in batch by OutputCollector
   * @throws IOException
   */
  void sink(TupleEntry tupleEntry, OutputCollector outputCollector)
          throws IOException;

  public static class Util {

    public static Mutation createColumnPutMutation(ByteBuffer name, ByteBuffer value) {
      Column column = new Column(name);
      column.setName(name);
      column.setValue(value);
      column.setTimestamp(System.currentTimeMillis());

      Mutation m = new Mutation();
      ColumnOrSuperColumn columnOrSuperColumn = new ColumnOrSuperColumn();
      columnOrSuperColumn.setColumn(column);
      m.setColumn_or_supercolumn(columnOrSuperColumn);

      return m;
    }
  }
}
