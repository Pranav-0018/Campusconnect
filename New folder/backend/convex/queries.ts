import { query, mutation } from "./_generated/server";
import { v } from "convex/values";
import { getAuthUserId } from "@convex-dev/auth/server";

export const create = mutation({
  args: { question: v.string() },
  handler: async (ctx, args) => {
    const userId = await getAuthUserId(ctx);
    if (!userId) {
      throw new Error("Not authenticated");
    }

    // Check if user is a student
    const profile = await ctx.db
      .query("profiles")
      .withIndex("by_user", (q) => q.eq("userId", userId))
      .first();

    if (profile?.role !== "student") {
      throw new Error("Only students can ask questions");
    }

    return await ctx.db.insert("queries", {
      studentId: userId,
      question: args.question,
    });
  },
});

export const answer = mutation({
  args: {
    queryId: v.id("queries"),
    answer: v.string(),
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
      throw new Error("Only teachers can answer questions");
    }

    return await ctx.db.patch(args.queryId, {
      answer: args.answer,
      answeredBy: userId,
      answeredAt: Date.now(),
    });
  },
});

export const list = query({
  args: {},
  handler: async (ctx) => {
    const queries = await ctx.db
      .query("queries")
      .order("desc")
      .collect();

    const queriesWithUsers = await Promise.all(
      queries.map(async (query) => {
        const student = await ctx.db.get(query.studentId);
        let teacher = null;
        if (query.answeredBy) {
          teacher = await ctx.db.get(query.answeredBy);
        }

        return {
          ...query,
          studentName: student?.name || "Unknown Student",
          teacherName: teacher?.name || null,
        };
      })
    );

    return queriesWithUsers;
  },
});
