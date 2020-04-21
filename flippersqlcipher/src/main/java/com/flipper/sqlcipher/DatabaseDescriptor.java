/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.flipper.sqlcipher;

/**
 * Interface to describe a Database object. The DatabaseDescriptor#name() is visible and displayed
 * to the user
 */
public interface DatabaseDescriptor {
  String name();
}
