/**
 * JumiBank front-end helpers (requires window.JUMIBANK_CTX from appGlobals.jsp).
 */
(function (global) {
  'use strict';

  function ctx() {
    return global.JUMIBANK_CTX || '';
  }

  /**
   * @param {string} path - Absolute path within the web app, e.g. "/services_proxy/bank/..."
   */
  function url(path) {
    if (!path) {
      return ctx();
    }
    var p = path.charAt(0) === '/' ? path : '/' + path;
    return ctx() + p;
  }

  /** Avoid double prefix when path already includes context path. */
  function resolveUrl(path) {
    if (!path) {
      return ctx();
    }
    if (path.indexOf('http') === 0) {
      return path;
    }
    var c = ctx();
    if (c && path.indexOf(c) === 0) {
      return path;
    }
    return url(path);
  }

  /**
   * @returns {{ status: number|string, message: string }}
   */
  function ajaxError(xhr) {
    var status = xhr && xhr.status > 0 ? xhr.status : 'timeout';
    var message =
      xhr && xhr.responseJSON && xhr.responseJSON.message
        ? xhr.responseJSON.message
        : xhr && xhr.responseText
          ? xhr.responseText
          : 'Server timeout';
    return { status: status, message: message };
  }

  function logAjaxError(xhr) {
    var e = ajaxError(xhr);
    global.console.error('Server returned ' + e.status + ': ' + e.message);
    return e;
  }

  /**
   * Low-level jQuery.ajax with URL resolved through {@link resolveUrl} when url is relative.
   * @param {JQueryAjaxSettings} config
   * @returns {JQuery.jqXHR|undefined}
   */
  function ajax(config) {
    var $ = global.jQuery;
    if (!$) {
      global.console.warn('JumiBank.ajax requires jQuery');
      return undefined;
    }
    var cfg = $.extend({}, config);
    if (cfg.url && cfg.url.indexOf('http') !== 0) {
      cfg.url = resolveUrl(cfg.url);
    }
    return $.ajax(cfg);
  }

  /**
   * GET JSON via jQuery (requires jQuery loaded before this script).
   * @param {string} path - App path starting with / or full URL
   * @param {{ timeout?: number }} [options]
   * @returns {JQuery.jqXHR|undefined}
   */
  function getJSON(path, options) {
    var $ = global.jQuery;
    if (!$) {
      global.console.warn('JumiBank.getJSON requires jQuery');
      return undefined;
    }
    options = options || {};
    var u = resolveUrl(path);
    return $.ajax({
      url: u,
      type: 'GET',
      dataType: 'json',
      timeout: options.timeout != null ? options.timeout : 30000
    });
  }

  /**
   * POST with Content-Type application/json and JSON response (optional body).
   */
  function postJSON(path, body, options) {
    var $ = global.jQuery;
    if (!$) {
      global.console.warn('JumiBank.postJSON requires jQuery');
      return undefined;
    }
    options = options || {};
    var u = resolveUrl(path);
    var cfg = {
      url: u,
      type: 'POST',
      contentType: 'application/json',
      dataType: 'json',
      timeout: options.timeout != null ? options.timeout : 30000
    };
    if (body !== undefined) {
      cfg.data = JSON.stringify(body);
    }
    return $.ajax(cfg);
  }

  /**
   * POST with no JSON body; response parsed as JSON (e.g. query-only REST POST).
   */
  function postExpectJSON(path, options) {
    var $ = global.jQuery;
    if (!$) {
      global.console.warn('JumiBank.postExpectJSON requires jQuery');
      return undefined;
    }
    options = options || {};
    var u = resolveUrl(path);
    return $.ajax({
      url: u,
      type: 'POST',
      dataType: 'json',
      timeout: options.timeout != null ? options.timeout : 30000
    });
  }

  /**
   * POST expecting text/plain or HTML response (jQuery).
   */
  function postText(path, options) {
    var $ = global.jQuery;
    if (!$) {
      global.console.warn('JumiBank.postText requires jQuery');
      return undefined;
    }
    options = options || {};
    var u = resolveUrl(path);
    return $.ajax({
      url: u,
      type: 'POST',
      timeout: options.timeout != null ? options.timeout : 30000,
      dataType: 'text'
    });
  }

  global.JumiBank = {
    ctx: ctx,
    url: url,
    resolveUrl: resolveUrl,
    ajax: ajax,
    ajaxError: ajaxError,
    logAjaxError: logAjaxError,
    getJSON: getJSON,
    postJSON: postJSON,
    postExpectJSON: postExpectJSON,
    postText: postText
  };
})(typeof window !== 'undefined' ? window : this);
