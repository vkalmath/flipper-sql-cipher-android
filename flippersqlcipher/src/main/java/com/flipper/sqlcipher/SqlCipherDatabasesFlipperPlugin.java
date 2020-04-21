/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.flipper.sqlcipher;

import android.content.Context;

import androidx.annotation.NonNull;

import com.facebook.flipper.core.FlipperConnection;
import com.facebook.flipper.core.FlipperPlugin;
import com.flipper.sqlcipher.impl.SqliteDatabaseDriver;

import java.util.Collections;
import java.util.List;

public class SqlCipherDatabasesFlipperPlugin implements FlipperPlugin {

  private static final String ID = "cipherdatabases";
  private final DatabasesManager databasesManager;

  public SqlCipherDatabasesFlipperPlugin(Context context, @NonNull String password, List<String> encryptedDbFileNames) {
    this(new SqliteDatabaseDriver(context, password, encryptedDbFileNames));
  }

  public SqlCipherDatabasesFlipperPlugin(DatabaseDriver databaseDriver) {
    this(Collections.singletonList(databaseDriver));
  }

  public SqlCipherDatabasesFlipperPlugin(List<DatabaseDriver> databaseDriverList) {
    databasesManager = new DatabasesManager(databaseDriverList);
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public void onConnect(FlipperConnection connection) {
    databasesManager.setConnection(connection);
  }

  @Override
  public void onDisconnect() {
    databasesManager.setConnection(null);
  }

  @Override
  public boolean runInBackground() {
    return false;
  }
}
