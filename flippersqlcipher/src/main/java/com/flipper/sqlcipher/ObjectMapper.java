/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.flipper.sqlcipher;

import android.text.TextUtils;

import com.facebook.flipper.core.FlipperArray;
import com.facebook.flipper.core.FlipperArray.Builder;
import com.facebook.flipper.core.FlipperObject;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObjectMapper {

  private static final int MAX_BLOB_LENGTH = 512;
  private static final String UNKNOWN_BLOB_LABEL = "{blob}";

  public static FlipperArray databaseListToFlipperArray(
      Collection<DatabasesManager.DatabaseDescriptorHolder> databaseDescriptorHolderList) {
    Builder builder = new Builder();

    for (DatabasesManager.DatabaseDescriptorHolder databaseDescriptorHolder : databaseDescriptorHolderList) {

      List<String> tableNameList =
          databaseDescriptorHolder.databaseDriver.getTableNames(
              databaseDescriptorHolder.databaseDescriptor);
      Collections.sort(tableNameList);
      Builder tableBuilder = new Builder();
      for (String tablename : tableNameList) {
        tableBuilder.put(tablename);
      }

      builder.put(
          new FlipperObject.Builder()
              .put("id", databaseDescriptorHolder.id)
              .put("name", databaseDescriptorHolder.databaseDescriptor.name())
              .put("tables", tableBuilder.build())
              .build());
    }

    return builder.build();
  }

  public static DatabasesManager.GetTableDataRequest flipperObjectToGetTableDataRequest(FlipperObject params) {
    int databaseId = params.getInt("databaseId");
    String table = params.getString("table");
    String order = params.getString("order");
    boolean reverse = params.getBoolean("reverse");
    int start = params.getInt("start");
    int count = params.getInt("count");
    if (databaseId <= 0 || TextUtils.isEmpty(table)) {
      return null;
    }
    return new DatabasesManager.GetTableDataRequest(databaseId, table, order, reverse, start, count);
  }

  public static DatabasesManager.GetTableStructureRequest flipperObjectToGetTableStructureRequest(
      FlipperObject params) {
    int databaseId = params.getInt("databaseId");
    String table = params.getString("table");
    if (databaseId <= 0 || TextUtils.isEmpty(table)) {
      return null;
    }
    return new DatabasesManager.GetTableStructureRequest(databaseId, table);
  }

  public static DatabasesManager.GetTableInfoRequest flipperObjectToGetTableInfoRequest(FlipperObject params) {
    int databaseId = params.getInt("databaseId");
    String table = params.getString("table");
    if (databaseId <= 0 || TextUtils.isEmpty(table)) {
      return null;
    }
    return new DatabasesManager.GetTableInfoRequest(databaseId, table);
  }

  public static DatabasesManager.ExecuteSqlRequest flipperObjectToExecuteSqlRequest(FlipperObject params) {
    int databaseId = params.getInt("databaseId");
    String value = params.getString("value");
    if (databaseId <= 0 || TextUtils.isEmpty(value)) {
      return null;
    }
    return new DatabasesManager.ExecuteSqlRequest(databaseId, value);
  }

  public static FlipperObject databaseGetTableDataReponseToFlipperObject(
      DatabaseDriver.DatabaseGetTableDataResponse databaseGetTableDataResponse) {

    Builder columnBuilder = new Builder();
    for (String columnName : databaseGetTableDataResponse.columns) {
      columnBuilder.put(columnName);
    }

    Builder rowBuilder = new Builder();
    for (List<Object> row : databaseGetTableDataResponse.values) {
      Builder valueBuilder = new Builder();
      for (Object item : row) {
        valueBuilder.put(objectAndTypeToFlipperObject(item));
      }
      rowBuilder.put(valueBuilder.build());
    }

    return new FlipperObject.Builder()
        .put("columns", columnBuilder.build())
        .put("values", rowBuilder.build())
        .put("start", databaseGetTableDataResponse.start)
        .put("count", databaseGetTableDataResponse.count)
        .put("total", databaseGetTableDataResponse.total)
        .build();
  }

  public static FlipperObject databaseGetTableStructureResponseToFlipperObject(
      DatabaseDriver.DatabaseGetTableStructureResponse databaseGetTableStructureResponse) {

    Builder structureColumnBuilder = new Builder();
    for (String columnName : databaseGetTableStructureResponse.structureColumns) {
      structureColumnBuilder.put(columnName);
    }

    Builder indexesColumnBuilder = new Builder();
    for (String columnName : databaseGetTableStructureResponse.indexesColumns) {
      indexesColumnBuilder.put(columnName);
    }

    Builder structureValuesBuilder = new Builder();
    for (List<Object> row : databaseGetTableStructureResponse.structureValues) {
      Builder valueBuilder = new Builder();
      for (Object item : row) {
        valueBuilder.put(objectAndTypeToFlipperObject(item));
      }
      structureValuesBuilder.put(valueBuilder.build());
    }

    Builder indexesValuesBuilder = new Builder();
    for (List<Object> row : databaseGetTableStructureResponse.indexesValues) {
      Builder valueBuilder = new Builder();
      for (Object item : row) {
        valueBuilder.put(objectAndTypeToFlipperObject(item));
      }
      indexesValuesBuilder.put(valueBuilder.build());
    }

    return new FlipperObject.Builder()
        .put("structureColumns", structureColumnBuilder.build())
        .put("structureValues", structureValuesBuilder.build())
        .put("indexesColumns", indexesColumnBuilder.build())
        .put("indexesValues", indexesValuesBuilder.build())
        .build();
  }

  public static FlipperObject databaseGetTableInfoResponseToFlipperObject(
      DatabaseDriver.DatabaseGetTableInfoResponse databaseGetTableInfoResponse) {

    return new FlipperObject.Builder()
        .put("definition", databaseGetTableInfoResponse.definition)
        .build();
  }

  public static FlipperObject databaseExecuteSqlResponseToFlipperObject(
      DatabaseDriver.DatabaseExecuteSqlResponse databaseExecuteSqlResponse) {

    Builder columnBuilder = new Builder();
    if (databaseExecuteSqlResponse.columns != null) {
      for (String columnName : databaseExecuteSqlResponse.columns) {
        columnBuilder.put(columnName);
      }
    }

    Builder rowBuilder = new Builder();
    if (databaseExecuteSqlResponse.values != null) {
      for (List<Object> row : databaseExecuteSqlResponse.values) {
        Builder valueBuilder = new Builder();
        for (Object item : row) {
          valueBuilder.put(objectAndTypeToFlipperObject(item));
        }
        rowBuilder.put(valueBuilder.build());
      }
    }

    return new FlipperObject.Builder()
        .put("type", databaseExecuteSqlResponse.type)
        .put("columns", columnBuilder.build())
        .put("values", rowBuilder.build())
        .put("insertedId", databaseExecuteSqlResponse.insertedId)
        .put("affectedCount", databaseExecuteSqlResponse.affectedCount)
        .build();
  }

  private static FlipperObject objectAndTypeToFlipperObject(Object object) {
    if (object == null) {
      return new FlipperObject.Builder().put("type", "null").build();
    } else if (object instanceof Long) {
      return new FlipperObject.Builder().put("type", "integer").put("value", object).build();
    } else if (object instanceof Double) {
      return new FlipperObject.Builder().put("type", "float").put("value", object).build();
    } else if (object instanceof String) {
      return new FlipperObject.Builder().put("type", "string").put("value", object).build();
    } else if (object instanceof byte[]) {
      return new FlipperObject.Builder()
          .put("type", "blob")
          .put("value", blobToString((byte[]) object))
          .build();
    } else if (object instanceof Boolean) {
      return new FlipperObject.Builder().put("type", "boolean").put("value", object).build();
    } else {
      throw new IllegalArgumentException("type of Object is invalid");
    }
  }

  private static String blobToString(byte[] blob) {
    if (blob.length <= MAX_BLOB_LENGTH) {
      if (fastIsAscii(blob)) {
        try {
          return new String(blob, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
          // Fall through...
        }
      }
    }
    return UNKNOWN_BLOB_LABEL;
  }

  private static boolean fastIsAscii(byte[] blob) {
    for (byte b : blob) {
      if ((b & ~0x7f) != 0) {
        return false;
      }
    }
    return true;
  }

  public static FlipperObject toErrorFlipperObject(int code, String message) {
    return new FlipperObject.Builder().put("code", code).put("message", message).build();
  }
}
