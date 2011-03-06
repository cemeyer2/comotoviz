/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.api.utility;

import edu.illinois.comoto.api.object.Cacheable;

import java.util.HashMap;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    2/10/11
 * Time:    7:47 PM
 * Package: edu.uiuc.cs.visualmoss.dataimport.api
 * Created by IntelliJ IDEA.
 * <p/>
 * This is a simple mechanism to cache results already received from the remote API
 * so that duplicate calls can be avoided. Caching is disabled by default to avoid
 * unexpected results (ie when fetching an object for a second time but requesting
 * more data from it). Caching can be enabled using the public functions.
 */
public class Cache {

    private static HashMap<Class, HashMap<Integer, Cacheable>> cache;
    private static boolean enabled;

    static {
        cache = new HashMap<Class, HashMap<Integer, Cacheable>>();
        enabled = false;
    }

    /**
     * Adds a cacheable object to the cache
     *
     * @param c the object to add
     * @return true if the object was added, false if it was already in the cache or caching is disabled
     * @see edu.illinois.comoto.api.object.Cacheable
     */
    public static boolean put(Cacheable c) {
        if (isEnabled() == false) {
            return false;
        }
        Class level1Key = c.getClass();
        int level2Key = c.getId();

        HashMap<Integer, Cacheable> level2Cache = getLevel2Cache(level1Key);
        if (!level2Cache.containsKey(level2Key)) {
            level2Cache.put(level2Key, c);
            return true;
        }
        return false;
    }

    /**
     * gets an object from the cache
     *
     * @param objectClass the class of the object you wish to retrieve
     * @param id          the identifier of the object
     * @return a Cacheable, which will need to be casted if the request was found in the cache, null if the object was not in the cache or caching is disabled
     * @see edu.illinois.comoto.api.object.Cacheable
     */
    public static Cacheable get(Class objectClass, int id) {
        if (isEnabled() == false) {
            return null;
        }
        HashMap<Integer, Cacheable> level2Cache = getLevel2Cache(objectClass);
        Cacheable c = level2Cache.get(id);
        return c;
    }

    /**
     * removes an object from the cache
     *
     * @param c the object to remove
     * @return true if the object was removed, false if it was not found in the cache or caching is disabled
     * @see edu.illinois.comoto.api.object.Cacheable
     */
    public static boolean remove(Cacheable c) {
        if (isEnabled() == false) {
            return false;
        }
        HashMap<Integer, Cacheable> level2Cache = getLevel2Cache(c.getClass());
        if (level2Cache.containsKey(c.getId())) {
            level2Cache.remove(c);
            return true;
        }
        return false;
    }

    private static HashMap<Integer, Cacheable> getLevel2Cache(Class key) {
        HashMap<Integer, Cacheable> level2Cache = cache.get(key);
        if (level2Cache == null) {
            level2Cache = new HashMap<Integer, Cacheable>();
            cache.put(key, level2Cache);
        }
        return level2Cache;
    }

    /**
     * @return if the cache is enabled or not
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * enables or disables caching
     *
     * @param enabled the flag enabling or disabling caching
     */
    public static void setEnabled(boolean enabled) {
        Cache.enabled = enabled;
    }

}
