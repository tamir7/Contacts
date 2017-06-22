package com.github.tamir7.contacts;

/**
 * author  dengyuhan
 * created 2017/6/19 17:27
 */
public interface ContactTransformer<T> {

    T transform(Contact source);

}
