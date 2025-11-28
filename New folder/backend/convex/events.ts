import { query, mutation } from "./_generated/server";
import { v } from "convex/values";
import { getAuthUserId } from "@convex-dev/auth/server";

export const create = mutation({
  args: {
    title: v.string(),
    description: v.string(),
    eventDate: v.string(),
  },
  handler: async (ctx, args) => {
    const userId = await getAuthUserId(ctx);
    if (!userId) {
      throw new Error("Not authenticated");
    }

    // Check if user is a teacher
    const profile = await ctx.db
      .query("profiles")
      .withIndex("by_user", (q) => q.eq("userId", userId))
      .first();

    if (profile?.role !== "teacher") {
      throw new Error("Only teachers can create events");
    }

    return await ctx.db.insert("events", {
      title: args.title,
      description: args.description,
      eventDate: args.eventDate,
      createdBy: userId,
    });
  },
});

export const list = query({
  args: {},
  handler: async (ctx) => {
    const events = await ctx.db
      .query("events")
      .order("desc")
      .collect();

    const eventsWithCreators = await Promise.all(
      events.map(async (event) => {
        const creator = await ctx.db.get(event.createdBy);
        return {
          ...event,
          createdByName: creator?.name || "Unknown Teacher",
        };
      })
    );

    return eventsWithCreators;
  },
});
