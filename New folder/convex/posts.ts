import { query, mutation } from "./_generated/server";
import { v } from "convex/values";
import { getAuthUserId } from "@convex-dev/auth/server";

export const create = mutation({
  args: { 
    content: v.string(),
    imageId: v.optional(v.id("_storage"))
  },
  handler: async (ctx, args) => {
    const userId = await getAuthUserId(ctx);
    if (!userId) {
      throw new Error("Not authenticated");
    }

    return await ctx.db.insert("posts", {
      userId,
      content: args.content,
      imageId: args.imageId,
    });
  },
});

export const generateUploadUrl = mutation({
  args: {},
  handler: async (ctx) => {
    return await ctx.storage.generateUploadUrl();
  },
});

export const list = query({
  args: {},
  handler: async (ctx) => {
    const posts = await ctx.db
      .query("posts")
      .order("desc")
      .collect();

    const postsWithUsers = await Promise.all(
      posts.map(async (post) => {
        const user = await ctx.db.get(post.userId);
        const profile = await ctx.db
          .query("profiles")
          .withIndex("by_user", (q) => q.eq("userId", post.userId))
          .first();
        
        let imageUrl = null;
        if (post.imageId) {
          imageUrl = await ctx.storage.getUrl(post.imageId);
        }
        
        return {
          ...post,
          userName: user?.name || "Unknown User",
          userRole: profile?.role || "student",
          imageUrl,
        };
      })
    );

    return postsWithUsers;
  },
});
