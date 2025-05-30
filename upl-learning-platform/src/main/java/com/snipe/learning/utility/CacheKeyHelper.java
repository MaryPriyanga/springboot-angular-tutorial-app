package com.snipe.learning.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CacheKeyHelper {

	    public static String generateCourseCacheKey(int page, int size) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userId = (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName()))
	                ? "anonymous"
	                : auth.getName();
	        System.out.println("Generated cache key: " + userId);
	        return "page_" + page + "_size_" + size + "_user_" + userId;
	    }
	    
	    public static String generateTutorialCacheKey(Long courseId, int page, int size) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userId = (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName()))
	                ? "anonymous"
	                : auth.getName();
	        return "course_" + courseId + "_page_" + page + "_size_" + size + "_user_" + userId;
	    }

}
