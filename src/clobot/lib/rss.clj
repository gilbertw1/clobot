(ns clobot.lib.rss
  (:import (com.sun.syndication.io SyndFeedInput XmlReader)
           (java.net URL)
           (java.io InputStreamReader)
           (com.sun.syndication.feed.synd SyndFeed))
  (:gen-class))

(defrecord Feed [authors categories contributors copyright description
           encoding entries feed-type image language link entry-links
           published-date title uri])
(defrecord Entry [authors categories contents contributors description
           enclosures link published-date title updated-date url])
(defrecord Enclosure [length type uri])
(defrecord Person [email name uri])
(defrecord Category [name taxonomyURI])
(defrecord Content [type value])
(defrecord Image [description link title url])
(defrecord Link [href hreflang length rel title type])

(defn make-enclosure "Create enclosure struct from SyndEnclosure"
  [e]
  (Enclosure. (.getLength e) (.getType e) (.getUrl e)))

(defn make-content "Create content struct from SyndContent"
  [c]
  (Content. (.getType c) (.getValue c)))

(defn make-link "Create link struct from SyndLink"
  [l]
  (Link. (.getHref l) 
         (.getHreflang l) 
         (.getLength l) 
         (.getRel l) 
         (.getTitle l) 
         (.getType l)))

(defn make-category "Create category struct from SyndCategory"
  [c]
  (Category. (.getName c) (.getTaxonomyUri c)))

(defn make-person "Create a person struct from SyndPerson"
  [sp]
  (Person. (.getEmail sp) (.getName sp) (.getUri sp)))

(defn make-image "Create image struct from SyndImage"
  [i]
  (Image. (.getDescription i)
          (.getLink i)
          (.getTitle i)
          (.getUrl i)))

(defn make-entry "Create feed entry struct from SyndEntry"
  [e]
  (Entry. (map make-person (seq (.getAuthors e)))
          (map make-category (seq (.getCategories e)))
          (map make-content (seq (.getContents e)))
          (map make-person (seq (.getContributors e)))
          (if-let [d (.getDescription e)] (make-content d))
          (map make-enclosure (seq (.getEnclosures e)))
          (.getLink e)
          (.getPublishedDate e)
          (.getTitle e)
          (.getUpdatedDate e)
          (.getUri e)))

(defn make-feed "Create a feed struct from a SyndFeed"
  [f]
  (Feed. (map make-person (seq (.getAuthors f)))
        (map make-category (seq (.getCategories f)))
        (map make-person (seq (.getContributors f)))
        (.getCopyright f)
        (.getDescription f)
        (.getEncoding f)
        (map make-entry (seq (.getEntries f)))
        (.getFeedType f)
        (if-let [i (.getImage f)] (make-image i))
        (.getLanguage f)
        (.getLink f)
        (map make-link (seq (.getLinks f)))
        (.getPublishedDate f)
        (.getTitle f)
        (.getUri f)))

(defn- parse-internal [xmlreader]
  (let [feedinput (new SyndFeedInput)
        syndfeed (.build feedinput xmlreader)]
    (make-feed syndfeed)))

(defn parse-feed "Get and parse a feed from a URL"
  ([feedsource]
     (parse-internal (new XmlReader (if (string? feedsource)
                                      (URL. feedsource)
                                      feedsource))))
  ([feedsource content-type]
     (parse-internal (new XmlReader feedsource content-type))))